package com.easybus.mappers;

import com.easybus.dtos.LoginResponse;
import com.easybus.dtos.UserProfileResponse;
import com.easybus.security.UserPrincipal;

public final class AuthMapper {
  private AuthMapper() {}

  public static LoginResponse toLoginResponse(UserPrincipal userDetails, String jwt) {
    return new LoginResponse(
        userDetails.getFullName(),
        userDetails.getEmail(),
        userDetails.getUsername(),
        userDetails.getAvatarUrl(),
        userDetails.getLocale(),
        userDetails.getEmailVerified(),
        userDetails.getRole() == null ? null : userDetails.getRole().name(),
        jwt);
  }

  public static UserProfileResponse toProfileResponse(UserPrincipal userDetails) {
    return new UserProfileResponse(
        userDetails.getFullName(),
        userDetails.getEmail(),
        userDetails.getUsername(),
        userDetails.getAvatarUrl(),
        userDetails.getLocale(),
        userDetails.getEmailVerified(),
        userDetails.getRole() == null ? null : userDetails.getRole().name());
  }
}
