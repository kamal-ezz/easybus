package com.easybus.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.easybus.dtos.BookingRequest;
import com.easybus.dtos.BookingResponse;
import com.easybus.entities.Booking;
import com.easybus.entities.Trip;
import com.easybus.enums.Equipment;
import com.easybus.enums.PaymentMethod;
import com.easybus.exceptions.BadRequestException;
import com.easybus.exceptions.ResourceNotFoundException;
import com.easybus.repository.BookingRepository;
import com.easybus.repository.TripRepository;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @Mock private TripRepository tripRepository;
  @Mock private BookingRepository bookingRepository;

  @InjectMocks private BookingService bookingService;

  private Trip sampleTrip;
  private BookingRequest validRequest;

  @BeforeEach
  void setUp() {
    sampleTrip =
        Trip.builder()
            .id(1L)
            .operator("CTM")
            .equipments(List.of(Equipment.AIR_CONDITIONER))
            .fromCity("casablanca")
            .toCity("rabat")
            .date(Date.valueOf("2026-02-15"))
            .departure(Time.valueOf("08:00:00"))
            .arrival(Time.valueOf("10:30:00"))
            .price(120.0)
            .availableSeats(new ArrayList<>(List.of("1A", "1B", "2A", "2B")))
            .build();

    validRequest =
        new BookingRequest(
            1L,
            "1A",
            "John Doe",
            "john@example.com",
            "+212612345678",
            PaymentMethod.PAYPAL,
            120.0,
            "MAD",
            "ORDER123");
  }

  @Test
  void createBooking_withValidData_createsBooking() {
    when(tripRepository.findById(1L)).thenReturn(Optional.of(sampleTrip));
    when(tripRepository.save(any(Trip.class))).thenReturn(sampleTrip);
    when(bookingRepository.save(any(Booking.class)))
        .thenAnswer(
            invocation -> {
              Booking b = invocation.getArgument(0);
              b.setId(100L);
              return b;
            });

    BookingResponse result = bookingService.createBooking(validRequest, null);

    assertNotNull(result);
    assertEquals(100L, result.id());
    assertEquals("1A", result.seat());
    assertEquals("John Doe", result.fullName());
    verify(tripRepository).save(any(Trip.class));
    verify(bookingRepository).save(any(Booking.class));
  }

  // Note: Non-PayPal payment method test removed - only PAYPAL is defined in PaymentMethod enum

  @Test
  void createBooking_withInvalidTripId_throwsException() {
    when(tripRepository.findById(999L)).thenReturn(Optional.empty());
    BookingRequest request =
        new BookingRequest(
            999L,
            "1A",
            "John Doe",
            "john@example.com",
            "+212612345678",
            PaymentMethod.PAYPAL,
            120.0,
            "MAD",
            "ORDER123");

    assertThrows(
        ResourceNotFoundException.class, () -> bookingService.createBooking(request, null));
  }

  @Test
  void createBooking_withUnavailableSeat_throwsException() {
    sampleTrip.setAvailableSeats(new ArrayList<>(List.of("2A", "2B")));
    when(tripRepository.findById(1L)).thenReturn(Optional.of(sampleTrip));

    assertThrows(BadRequestException.class, () -> bookingService.createBooking(validRequest, null));
  }

  @Test
  void createBookingsForPaypalOrder_withValidData_createsMultipleBookings() {
    when(tripRepository.findById(1L)).thenReturn(Optional.of(sampleTrip));
    when(tripRepository.save(any(Trip.class))).thenReturn(sampleTrip);
    when(bookingRepository.save(any(Booking.class)))
        .thenAnswer(
            invocation -> {
              Booking b = invocation.getArgument(0);
              b.setId((long) (Math.random() * 1000));
              return b;
            });

    List<Long> result =
        bookingService.createBookingsForPaypalOrder(
            1L,
            List.of("1A", "1B"),
            "John Doe",
            "john@example.com",
            "+212612345678",
            240.0,
            "MAD",
            "ORDER123");

    assertEquals(2, result.size());
    verify(bookingRepository, times(2)).save(any(Booking.class));
  }

  @Test
  void createBookingsForPaypalOrder_withPartiallyUnavailableSeats_throwsException() {
    sampleTrip.setAvailableSeats(new ArrayList<>(List.of("1A", "2A")));
    when(tripRepository.findById(1L)).thenReturn(Optional.of(sampleTrip));

    assertThrows(
        BadRequestException.class,
        () ->
            bookingService.createBookingsForPaypalOrder(
                1L,
                List.of("1A", "1B"),
                "John Doe",
                "john@example.com",
                "+212612345678",
                240.0,
                "MAD",
                "ORDER123"));
  }
}
