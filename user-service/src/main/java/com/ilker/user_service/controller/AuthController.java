package com.ilker.user_service.controller;

import com.ilker.user_service.entitiy.User;
import com.ilker.user_service.repository.UserRepository;
import com.ilker.user_service.request.LoginRequest;
import com.ilker.user_service.response.AuthResponse;
import com.ilker.user_service.service.AuthService;
import com.ilker.user_service.service.CustomerUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody User user){
        return ResponseEntity.ok(authService.createUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.signIn(request));
    }



}
