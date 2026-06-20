package com.easybus.paypal;

import java.util.List;

public record PendingBooking(
    Long tripId,
    List<String> seats,
    String fullName,
    String email,
    String phone,
    Double amount,
    String currency) {}
