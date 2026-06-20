package com.easybus.dtos;

import jakarta.validation.constraints.NotBlank;

public record PaypalCaptureRequest(@NotBlank String orderId) {}
