package com.kamal.easybus.web;

import javax.validation.Valid;

import com.kamal.easybus.dtos.LoginRequestDTO;
import com.kamal.easybus.dtos.LoginResponseDTO;
import com.kamal.easybus.enums.Role;
import com.kamal.easybus.entities.User;
import com.kamal.easybus.security.UserPrincipal;
import com.kamal.easybus.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamal.easybus.repos.UserRepo;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    JwtTokenProvider tokenProvider;
    UserRepo userRepo;
    PasswordEncoder passwordEncoder;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        //User loggedUser = userRepo.findById(tokenProvider.getUserIdFromJWT(jwt)).get();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(new LoginResponseDTO(
                    userDetails.getFirstName(),
                    userDetails.getLastName(),
                    userDetails.getEmail(),
                    userDetails.getPhone(),
                    jwt
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> signup(@Valid @RequestBody User signUpRequest) {
        if(userRepo.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Email Address already in use!",
                    HttpStatus.BAD_REQUEST);
        }
        User user = new User(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPhone());

        user.setRoles(Set.of(Role.USER));
        User result = userRepo.save(user);
        return ResponseEntity.ok(result);
    }
}
