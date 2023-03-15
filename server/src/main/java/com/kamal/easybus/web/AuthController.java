package com.kamal.easybus.web;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.kamal.easybus.dtos.LoginRequestDTO;
import com.kamal.easybus.dtos.LoginResponseDTO;
import com.kamal.easybus.enums.Role;
import com.kamal.easybus.entities.User;
import com.kamal.easybus.exceptions.BadRequestException;
import com.kamal.easybus.security.UserPrincipal;
import com.kamal.easybus.security.jwt.JwtTokenProvider;
import com.kamal.easybus.services.AuthService;
import com.kamal.easybus.services.MailService;
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

    AuthService authService;
    MailService mailService;



    @Autowired
    public AuthController(AuthService authService, MailService mailService) {
        this.authService = authService;
        this.mailService = mailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authService.login(loginRequest);
        String jwt = authService.generateToken(authentication);
        UserPrincipal userDetails = authService.getUserDetails(authentication);
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
        try {
            Authentication authentication = authService.register(signUpRequest);
            String jwt = authService.generateToken(authentication);
            UserPrincipal userDetails = authService.getUserDetails(authentication);
            //mailService.sendWelcomeEmail(userDetails.getEmail());
            return ResponseEntity.ok(new LoginResponseDTO(
                    userDetails.getFirstName(),
                    userDetails.getLastName(),
                    userDetails.getEmail(),
                    userDetails.getPhone(),
                    jwt
            ));
        } catch (BadRequestException e) {
            return new ResponseEntity<>("Email Address already in use!",
                    HttpStatus.BAD_REQUEST);
        }/* catch (MessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error while sending email",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
    }
}
