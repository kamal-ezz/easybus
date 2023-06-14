package com.kamal.easybus.services;

import com.kamal.easybus.dtos.LoginRequest;
import com.kamal.easybus.repos.AdminRepo;
import com.kamal.easybus.security.UserPrincipal;
import com.kamal.easybus.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    AuthenticationManager authenticationManager;
    AdminRepo userRepo;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider tokenProvider;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, AdminRepo userRepo, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public Authentication login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }


    public String generateToken(Authentication authentication){
        return tokenProvider.generateToken(authentication);
    }

    public UserPrincipal getUserDetails(Authentication authentication){
        return (UserPrincipal) authentication.getPrincipal();
    }
}
