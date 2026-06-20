package com.easybus.dtos;

import java.util.List;

public record PaypalCaptureResponse(boolean success, List<Long> bookingIds, String message) {}
