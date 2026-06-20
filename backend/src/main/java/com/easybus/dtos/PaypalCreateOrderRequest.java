package com.easybus.dtos;

import jakarta.validation.constraints.*;
import java.util.List;

public record PaypalCreateOrderRequest(
    @NotNull Long tripId,
    @NotEmpty List<String> seats,
    @NotBlank String fullName,
    @NotBlank @Email String email,
    @NotBlank String phone,
    @NotNull @Positive Double amount,
    @NotBlank String currency,
    @NotBlank String returnUrl,
    @NotBlank String cancelUrl) {}
