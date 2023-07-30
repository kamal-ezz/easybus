package com.kamal.easybus.web;

import javax.validation.Valid;

import com.kamal.easybus.dtos.LoginRequest;
import com.kamal.easybus.dtos.LoginResponse;
import com.kamal.easybus.security.UserPrincipal;
import com.kamal.easybus.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authService.login(loginRequest);
        String jwt = authService.generateToken(authentication);
        UserPrincipal userDetails = authService.getUserDetails(authentication);
        return ResponseEntity.ok(new LoginResponse(
                    userDetails.getFullName(),
                    userDetails.getEmail(),
                    jwt
        ));
    }


    @GetMapping("/logged")
    public ResponseEntity<Boolean> isLoggedIn(){
        return ResponseEntity.ok(authService.isLoggedIn());
    }


}
