package com.easybus.services;

import com.easybus.dtos.GoogleAuthRequest;
import com.easybus.dtos.LoginRequest;
import com.easybus.entities.User;
import com.easybus.enums.Role;
import com.easybus.exceptions.BadRequestException;
import com.easybus.mock.MockDataProvider;
import com.easybus.repository.UserRepository;
import com.easybus.security.UserPrincipal;
import com.easybus.security.jwt.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
  private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final JwtTokenProvider tokenProvider;

  @Value("${app.mock-mode:false}")
  private boolean mockMode;

  @Autowired(required = false)
  private MockDataProvider mockDataProvider;

  @Value("${app.google.clientId}")
  private String googleClientId;

  @Value("${app.google.clientSecret}")
  private String googleClientSecret;

  @Value("${app.redirect-uri}")
  private String redirectUri;

  private GoogleIdTokenVerifier googleIdTokenVerifier;
  private NetHttpTransport httpTransport;
  private GsonFactory jsonFactory;

  @PostConstruct
  private void initVerifier() {
    this.httpTransport = new NetHttpTransport();
    this.jsonFactory = GsonFactory.getDefaultInstance();
    this.googleIdTokenVerifier =
        new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
            .setAudience(java.util.List.of(googleClientId))
            .build();
  }

  public String getGoogleAuthUrl() {
    return GOOGLE_AUTH_URL
        + "?client_id="
        + URLEncoder.encode(googleClientId, StandardCharsets.UTF_8)
        + "&redirect_uri="
        + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
        + "&response_type=code"
        + "&scope="
        + URLEncoder.encode("openid email profile", StandardCharsets.UTF_8)
        + "&access_type=offline"
        + "&prompt=consent";
  }

  @SneakyThrows
  public Authentication authenticateGoogleCode(String code) {
    // In mock mode, return mock user without calling Google
    if (mockMode && mockDataProvider != null) {
      return authenticateMockUser();
    }

    var tokenResponse =
        new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                jsonFactory,
                GOOGLE_TOKEN_URL,
                googleClientId,
                googleClientSecret,
                code,
                redirectUri)
            .execute();

    var idTokenString = tokenResponse.getIdToken();
    if (idTokenString == null) {
      throw new BadRequestException("No ID token received from Google.");
    }

    var payload = verifyGoogleToken(idTokenString);
    var user = upsertGoogleUser(payload);
    return authenticateUser(user);
  }

  public Authentication login(LoginRequest loginRequest) {
    var authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return authentication;
  }

  @SneakyThrows
  public Authentication authenticateGoogle(GoogleAuthRequest googleAuthRequest) {
    // In mock mode, return mock user without validating token
    if (mockMode && mockDataProvider != null) {
      return authenticateMockUser();
    }

    GoogleIdToken.Payload payload = verifyGoogleToken(googleAuthRequest.idToken());
    var user = upsertGoogleUser(payload);
    return authenticateUser(user);
  }

  private Authentication authenticateMockUser() {
    var principal = mockDataProvider.getMockUser();
    var authentication =
        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return authentication;
  }

  public String generateToken(Authentication authentication) {
    return tokenProvider.generateToken(authentication);
  }

  public UserPrincipal getUserDetails(Authentication authentication) {
    return (UserPrincipal) authentication.getPrincipal();
  }

  @SneakyThrows
  private GoogleIdToken.Payload verifyGoogleToken(String idToken) {
    var token = googleIdTokenVerifier.verify(idToken);
    if (token == null) {
      throw new BadRequestException("Invalid Google ID token.");
    }
    return token.getPayload();
  }

  private User upsertGoogleUser(GoogleIdToken.Payload payload) {
    var email = payload.getEmail();
    var googleSub = payload.getSubject();
    var fullName = (String) payload.get("name");
    var givenName = (String) payload.get("given_name");
    var avatarUrl = (String) payload.get("picture");
    var emailVerified = payload.getEmailVerified();
    var locale = (String) payload.get("locale");

    // Try to find existing user by Google Sub first, then by email
    var existingUser = userRepository.findByGoogleSub(googleSub).orElse(null);
    if (existingUser == null && email != null) {
      existingUser = userRepository.findByEmail(email).orElse(null);
      if (existingUser != null && existingUser.getRole() == Role.ADMIN) {
        throw new BadRequestException("Admin accounts must use admin login.");
      }
    }

    User user;
    if (existingUser != null) {
      // Update existing user - keep username consistent with email
      existingUser.setEmail(email);
      existingUser.setFullName(fullName);
      // Always sync username with email prefix to ensure consistency
      existingUser.setUsername(buildUsername(email, givenName));
      existingUser.setAvatarUrl(avatarUrl);
      existingUser.setEmailVerified(emailVerified);
      existingUser.setLocale(locale);
      existingUser.setGoogleSub(googleSub);
      // Keep existing role if user already has one
      user = existingUser;
    } else {
      // Create new user
      user =
          User.builder()
              .email(email)
              .fullName(fullName)
              .username(buildUsername(email, givenName))
              .avatarUrl(avatarUrl)
              .emailVerified(emailVerified)
              .locale(locale)
              .googleSub(googleSub)
              .role(Role.USER)
              .build();
    }

    return userRepository.save(user);
  }

  private Authentication authenticateUser(User user) {
    var principal = UserPrincipal.create(user);
    var authentication =
        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return authentication;
  }

  private String buildUsername(String email, String givenName) {
    // Prefer email prefix over given name for more unique usernames
    if (email != null && email.contains("@")) {
      return email.substring(0, email.indexOf('@'));
    }
    if (givenName != null && !givenName.isBlank()) {
      return givenName.trim();
    }
    return "user";
  }
}
