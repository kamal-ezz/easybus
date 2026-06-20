package com.easybus.dtos;

import com.easybus.enums.BookingStatus;
import com.easybus.enums.PaymentMethod;
import java.time.Instant;

public record BookingResponse(
    Long id,
    Long tripId,
    String seat,
    String fullName,
    String email,
    String phone,
    Double amount,
    String currency,
    String paypalOrderId,
    PaymentMethod paymentMethod,
    BookingStatus bookingStatus,
    Instant createdAt) {}
