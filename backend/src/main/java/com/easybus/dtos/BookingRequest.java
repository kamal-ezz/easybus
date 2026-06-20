package com.easybus.dtos;

import com.easybus.enums.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookingRequest(
    @NotNull Long tripId,
    @NotNull String seat,
    @NotBlank String fullName,
    @NotBlank @Email String email,
    @NotBlank String phone,
    @NotNull PaymentMethod paymentMethod,
    @NotNull @Positive Double amount,
    @NotBlank String currency,
    @NotBlank String paypalOrderId) {}
