package com.easybus.controllers;

import com.easybus.dtos.BookingRequest;
import com.easybus.dtos.BookingResponse;
import com.easybus.security.UserPrincipal;
import com.easybus.services.BookingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/bookings", "/api/book"})
@RequiredArgsConstructor
public class BookingController {

  private final BookingService bookingService;

  @PostMapping
  public ResponseEntity<BookingResponse> book(
      @Valid @RequestBody BookingRequest request, Authentication authentication) {
    var userPrincipal =
        authentication != null && authentication.getPrincipal() instanceof UserPrincipal
            ? (UserPrincipal) authentication.getPrincipal()
            : null;
    return ResponseEntity.ok(bookingService.createBooking(request, userPrincipal));
  }

  @GetMapping("/my")
  public ResponseEntity<List<BookingResponse>> myBookings(Authentication authentication) {
    if (authentication == null
        || !(authentication.getPrincipal() instanceof UserPrincipal userPrincipal)) {
      return ResponseEntity.status(401).build();
    }
    return ResponseEntity.ok(bookingService.getUserBookings(userPrincipal.getId()));
  }
}
