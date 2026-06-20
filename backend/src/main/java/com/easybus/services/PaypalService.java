package com.easybus.services;

import com.easybus.dtos.PaypalCaptureResponse;
import com.easybus.dtos.PaypalCreateOrderRequest;
import com.easybus.dtos.PaypalCreateOrderResponse;
import com.easybus.exceptions.BadRequestException;
import com.easybus.paypal.PendingBooking;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaypalService {

  // Sandbox only - live mode removed for demo purposes
  private static final String TOKEN_URL = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
  private static final String ORDERS_URL = "https://api-m.sandbox.paypal.com/v2/checkout/orders";

  private final RestTemplate restTemplate = new RestTemplate();
  private final BookingService bookingService;

  private final String clientId;
  private final String clientSecret;

  private final Map<String, PendingBooking> pendingBookings = new ConcurrentHashMap<>();

  public PaypalService(
      @Value("${paypal.clientId:}") String clientId,
      @Value("${paypal.clientSecret:}") String clientSecret,
      BookingService bookingService) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.bookingService = bookingService;
  }

  public PaypalCreateOrderResponse createOrder(PaypalCreateOrderRequest request) {
    if (clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
      throw new BadRequestException(
          "PayPal is not configured. Set paypal.clientId and paypal.clientSecret.");
    }
    String accessToken = getAccessToken();

    String payload =
        """
        {
          "intent": "CAPTURE",
          "purchase_units": [{
            "amount": {
              "currency_code": "%s",
              "value": "%.2f"
            },
            "description": "EasyBus - Bus ticket reservation"
          }],
          "application_context": {
            "return_url": "%s",
            "cancel_url": "%s"
          }
        }
        """
            .formatted(
                request.currency(), request.amount(), request.returnUrl(), request.cancelUrl());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(accessToken);

    ResponseEntity<Map> response =
        restTemplate.exchange(
            ORDERS_URL, HttpMethod.POST, new HttpEntity<>(payload, headers), Map.class);

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      String orderId = (String) response.getBody().get("id");
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> links = (List<Map<String, Object>>) response.getBody().get("links");
      String approvalUrl =
          links.stream()
              .filter(l -> "approve".equals(l.get("rel")))
              .map(l -> (String) l.get("href"))
              .findFirst()
              .orElseThrow(() -> new BadRequestException("PayPal did not return approval URL."));

      pendingBookings.put(
          orderId,
          new PendingBooking(
              request.tripId(),
              request.seats(),
              request.fullName(),
              request.email(),
              request.phone(),
              request.amount(),
              request.currency()));

      return new PaypalCreateOrderResponse(orderId, approvalUrl);
    }
    throw new BadRequestException("Failed to create PayPal order.");
  }

  public PaypalCaptureResponse captureOrder(String orderId) {
    PendingBooking pending = pendingBookings.remove(orderId);
    if (pending == null) {
      return new PaypalCaptureResponse(false, List.of(), "Invalid or expired order.");
    }

    if (clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
      return new PaypalCaptureResponse(false, List.of(), "PayPal is not configured.");
    }

    String accessToken = getAccessToken();
    String captureUrl = ORDERS_URL + "/" + orderId + "/capture";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(accessToken);

    ResponseEntity<Map> response =
        restTemplate.exchange(
            captureUrl, HttpMethod.POST, new HttpEntity<>("{}", headers), Map.class);

    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
      pendingBookings.put(orderId, pending);
      return new PaypalCaptureResponse(false, List.of(), "PayPal capture failed.");
    }

    String status = (String) response.getBody().get("status");
    if (!"COMPLETED".equals(status)) {
      pendingBookings.put(orderId, pending);
      return new PaypalCaptureResponse(false, List.of(), "Payment not completed: " + status);
    }

    List<Long> bookingIds =
        bookingService.createBookingsForPaypalOrder(
            pending.tripId(),
            pending.seats(),
            pending.fullName(),
            pending.email(),
            pending.phone(),
            pending.amount(),
            pending.currency(),
            orderId);

    return new PaypalCaptureResponse(true, bookingIds, "Payment and booking completed.");
  }

  private String getAccessToken() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBasicAuth(clientId, clientSecret);
    HttpEntity<String> entity = new HttpEntity<>("grant_type=client_credentials", headers);
    ResponseEntity<Map> response =
        restTemplate.exchange(TOKEN_URL, HttpMethod.POST, entity, Map.class);
    if (response.getBody() != null && response.getBody().get("access_token") != null) {
      return (String) response.getBody().get("access_token");
    }
    throw new BadRequestException("Failed to get PayPal access token.");
  }
}
