package com.ilker.user_service.service;

import com.ilker.user_service.config.JwtProvider;
import com.ilker.user_service.entitiy.User;
import com.ilker.user_service.exception.UserAlreadyExistsException;
import com.ilker.user_service.repository.UserRepository;
import com.ilker.user_service.request.LoginRequest;
import com.ilker.user_service.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerUserService customerUserService;

    public AuthResponse createUser(User user){

        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String role = user.getRole();

        User isEmailExist = userRepository.findByEmail(email);

        if(isEmailExist != null){
            throw  new UserAlreadyExistsException("Email is already used with another account.");
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFullName(fullName);
        createdUser.setRole(role);

        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register success.");
        authResponse.setStatus(true);

        return authResponse;
    }

    public AuthResponse signIn(LoginRequest request){
        String userName = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticate(userName,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login success.");
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        return authResponse;
    }



    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customerUserService.loadUserByUsername(userName);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid username or password.");
        }

        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid username or password.");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
