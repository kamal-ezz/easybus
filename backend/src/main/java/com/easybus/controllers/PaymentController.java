package com.easybus.controllers;

import com.easybus.dtos.*;
import com.easybus.services.PaypalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal")
@RequiredArgsConstructor
public class PaymentController {

  private final PaypalService paypalService;

  @PostMapping("/create-order")
  public ResponseEntity<PaypalCreateOrderResponse> createOrder(
      @Valid @RequestBody PaypalCreateOrderRequest request) {
    return ResponseEntity.ok(paypalService.createOrder(request));
  }

  @PostMapping("/capture")
  public ResponseEntity<PaypalCaptureResponse> capture(
      @Valid @RequestBody PaypalCaptureRequest request) {
    return ResponseEntity.ok(paypalService.captureOrder(request.orderId()));
  }
}
