package com.ilker.user_service.service;

import com.ilker.user_service.config.JwtProvider;
import com.ilker.user_service.entitiy.User;
import com.ilker.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

     public User getUserProfile(String jwt){
         String email = JwtProvider.getEmailFromJwtToken(jwt);
         User user = userRepository.findByEmail(email);
         return user;
     }

     public List<User> getAllUsers(){
         List<User> users = userRepository.findAll();
         return users;
     }
}
