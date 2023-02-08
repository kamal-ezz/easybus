package com.kamal.easybus.services;

import com.kamal.easybus.dtos.LoginRequestDTO;
import com.kamal.easybus.entities.User;
import com.kamal.easybus.enums.Role;
import com.kamal.easybus.exceptions.BadRequestException;
import com.kamal.easybus.repos.UserRepo;
import com.kamal.easybus.security.UserPrincipal;
import com.kamal.easybus.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    AuthenticationManager authenticationManager;
    UserRepo userRepo;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider tokenProvider;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserRepo userRepo, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    //User loggedUser = userRepo.findById(tokenProvider.getUserIdFromJWT(jwt)).get();

    public Authentication login(LoginRequestDTO loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public Authentication register(User signUpRequest){
        if(userRepo.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email Address already in use!");
        }
        User user = new User(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPhone());

        user.setRoles(Set.of(Role.USER));
        userRepo.save(user);

        //login after signup
        LoginRequestDTO loginRequest = new LoginRequestDTO(user.getEmail(), user.getPassword());
        return login(loginRequest);
    }


    public String generateToken(Authentication authentication){
        return tokenProvider.generateToken(authentication);
    }

    public UserPrincipal getUserDetails(Authentication authentication){
        return (UserPrincipal) authentication.getPrincipal();
    }
}
