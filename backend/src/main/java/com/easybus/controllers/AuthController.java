package com.easybus.controllers;

import com.easybus.dtos.GoogleAuthRequest;
import com.easybus.dtos.LoginRequest;
import com.easybus.dtos.UserProfileResponse;
import com.easybus.mappers.AuthMapper;
import com.easybus.security.UserPrincipal;
import com.easybus.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @Value("${app.frontend.url:http://localhost:4200}")
  private String frontendUrl;

  @PostMapping("/admin/login")
  public ResponseEntity<?> adminLogin(@Valid @RequestBody LoginRequest loginRequest) {
    var authentication = authService.login(loginRequest);
    var jwt = authService.generateToken(authentication);
    var userDetails = authService.getUserDetails(authentication);
    return ResponseEntity.ok(AuthMapper.toLoginResponse(userDetails, jwt));
  }

  @PostMapping("/google")
  public ResponseEntity<?> google(@Valid @RequestBody GoogleAuthRequest googleAuthRequest) {
    var authentication = authService.authenticateGoogle(googleAuthRequest);
    var jwt = authService.generateToken(authentication);
    var userDetails = authService.getUserDetails(authentication);
    return ResponseEntity.ok(AuthMapper.toLoginResponse(userDetails, jwt));
  }

  @GetMapping("/me")
  public ResponseEntity<UserProfileResponse> me(Authentication authentication) {
    var userDetails = (UserPrincipal) authentication.getPrincipal();
    return ResponseEntity.ok(AuthMapper.toProfileResponse(userDetails));
  }

  @GetMapping("/google/redirect")
  public void googleRedirect(HttpServletResponse response) throws IOException {
    var authUrl = authService.getGoogleAuthUrl();
    response.sendRedirect(authUrl);
  }

  @GetMapping("/google/callback")
  public void googleCallback(
      @RequestParam("code") String code,
      @RequestParam(value = "error", required = false) String error,
      HttpServletResponse response)
      throws IOException {
    if (error != null) {
      response.sendRedirect(
          frontendUrl + "/auth/error?message=" + URLEncoder.encode(error, StandardCharsets.UTF_8));
      return;
    }

    try {
      var authentication = authService.authenticateGoogleCode(code);
      var jwt = authService.generateToken(authentication);
      var userDetails = authService.getUserDetails(authentication);
      var user = AuthMapper.toLoginResponse(userDetails, jwt);

      // Redirect to frontend with token
      var redirectUrl =
          frontendUrl
              + "/auth/callback?token="
              + URLEncoder.encode(jwt, StandardCharsets.UTF_8)
              + "&user="
              + URLEncoder.encode(toJson(user), StandardCharsets.UTF_8);
      response.sendRedirect(redirectUrl);
    } catch (Exception e) {
      response.sendRedirect(
          frontendUrl
              + "/auth/error?message="
              + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
    }
  }

  private String toJson(Object obj) {
    try {
      return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      return "{}";
    }
  }
}
