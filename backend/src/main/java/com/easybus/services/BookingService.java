package com.easybus.services;

import com.easybus.dtos.BookingRequest;
import com.easybus.dtos.BookingResponse;
import com.easybus.entities.Booking;
import com.easybus.entities.Trip;
import com.easybus.enums.BookingStatus;
import com.easybus.enums.PaymentMethod;
import com.easybus.exceptions.BadRequestException;
import com.easybus.exceptions.ResourceNotFoundException;
import com.easybus.mock.MockDataProvider;
import com.easybus.repository.BookingRepository;
import com.easybus.repository.TripRepository;
import com.easybus.security.UserPrincipal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {
  private final TripRepository tripRepository;
  private final BookingRepository bookingRepository;

  @Value("${app.mock-mode:false}")
  private boolean mockMode;

  @Autowired(required = false)
  private MockDataProvider mockDataProvider;

  @Transactional
  public BookingResponse createBooking(BookingRequest request, UserPrincipal userPrincipal) {
    if (request.paymentMethod() != PaymentMethod.PAYPAL) {
      throw new BadRequestException("Only PAYPAL is supported.");
    }

    // In mock mode, return mock booking without DB operations
    if (mockMode && mockDataProvider != null) {
      return mockDataProvider.createMockBooking(request);
    }

    var trip =
        tripRepository
            .findById(request.tripId())
            .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", request.tripId()));

    var availableSeats = trip.getAvailableSeats();
    if (availableSeats == null || availableSeats.isEmpty()) {
      throw new BadRequestException("No available seats for this trip.");
    }
    if (!availableSeats.contains(request.seat())) {
      throw new BadRequestException("Selected seat is not available.");
    }

    var updatedSeats = new ArrayList<>(availableSeats);
    updatedSeats.remove(request.seat());
    trip.setAvailableSeats(updatedSeats);
    tripRepository.save(trip);

    var booking = getBooking(request, userPrincipal, trip);

    var saved = bookingRepository.save(booking);

    return new BookingResponse(
        saved.getId(),
        saved.getTrip().getId(),
        saved.getSeat(),
        saved.getFullName(),
        saved.getEmail(),
        saved.getPhone(),
        saved.getAmount(),
        saved.getCurrency(),
        saved.getPaypalOrderId(),
        saved.getPaymentMethod(),
        saved.getBookingStatus(),
        saved.getCreatedAt());
  }

  @Transactional
  public List<Long> createBookingsForPaypalOrder(
      Long tripId,
      List<String> seats,
      String fullName,
      String email,
      String phone,
      Double amount,
      String currency,
      String paypalOrderId) {
    // In mock mode, return mock booking IDs without DB operations
    if (mockMode && mockDataProvider != null) {
      return mockDataProvider.createMockBookingsForPaypal(
          tripId, seats, fullName, email, phone, amount, currency, paypalOrderId);
    }

    var trip =
        tripRepository
            .findById(tripId)
            .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", tripId));
    var availableSeats = trip.getAvailableSeats();
    if (availableSeats == null || availableSeats.isEmpty()) {
      throw new BadRequestException("No available seats for this trip.");
    }
    for (String seat : seats) {
      if (!availableSeats.contains(seat)) {
        throw new BadRequestException("Seat " + seat + " is not available.");
      }
    }
    var updatedSeats = new ArrayList<>(availableSeats);
    updatedSeats.removeAll(seats);
    trip.setAvailableSeats(updatedSeats);
    tripRepository.save(trip);

    List<Long> ids = new ArrayList<>();
    double amountPerSeat = amount / seats.size();
    for (String seat : seats) {
      var req =
          new BookingRequest(
              tripId,
              seat,
              fullName,
              email,
              phone,
              PaymentMethod.PAYPAL,
              amountPerSeat,
              currency,
              paypalOrderId);
      var booking = getBooking(req, null, trip);
      booking.setBookingStatus(BookingStatus.CONFIRMED);
      var saved = bookingRepository.save(booking);
      ids.add(saved.getId());
    }
    return ids;
  }

  public List<BookingResponse> getUserBookings(Long userId) {
    return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(
            b ->
                new BookingResponse(
                    b.getId(),
                    b.getTrip() != null ? b.getTrip().getId() : null,
                    b.getSeat(),
                    b.getFullName(),
                    b.getEmail(),
                    b.getPhone(),
                    b.getAmount(),
                    b.getCurrency(),
                    b.getPaypalOrderId(),
                    b.getPaymentMethod(),
                    b.getBookingStatus(),
                    b.getCreatedAt()))
        .toList();
  }

  private static @NonNull Booking getBooking(
      BookingRequest request, UserPrincipal userPrincipal, Trip trip) {
    var booking = new Booking();
    booking.setTrip(trip);
    booking.setSeat(request.seat());
    booking.setFullName(request.fullName());
    booking.setEmail(request.email());
    booking.setPhone(request.phone());
    booking.setPaymentMethod(request.paymentMethod());
    booking.setAmount(request.amount());
    booking.setCurrency(request.currency());
    booking.setPaypalOrderId(request.paypalOrderId());
    booking.setBookingStatus(BookingStatus.IN_PROGRESS);
    if (userPrincipal != null) {
      var user = new com.easybus.entities.User();
      user.setId(userPrincipal.getId());
      booking.setUser(user);
    }
    return booking;
  }
}
