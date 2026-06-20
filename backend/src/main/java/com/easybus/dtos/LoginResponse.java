package com.easybus.dtos;

public record LoginResponse(
    String fullName,
    String email,
    String username,
    String avatarUrl,
    String locale,
    Boolean emailVerified,
    String role,
    String token) {}
