package com.easybus.dtos;

public record UserProfileResponse(
    String fullName,
    String email,
    String username,
    String avatarUrl,
    String locale,
    Boolean emailVerified,
    String role) {}
