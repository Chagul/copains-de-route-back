package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.repository.UserRepository;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.*;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.UserResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.User;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public List<UserResponseDTOs> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTOs> userResponseDTOs = new ArrayList<>();
        for (User user : users) {
           UserResponseDTOs userResponseDTO = new UserResponseDTOs(user);
           userResponseDTOs.add(userResponseDTO);
            
        }
        return userResponseDTOs;
    }

    public User createUser(UserRegisterRequestDTOs userRequestDTO) {
    
        User user = new User();

        user.setLogin(userRequestDTO.getLogin());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

       return userRepository.save(user);
    }

    public UserResponseDTOs getUserByLogin(String login) throws RuntimeException{
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return new UserResponseDTOs(user);
    }

    public User updateUser(String loginChange, String userLogin) throws RuntimeException{
        
        User user = userRepository.findByLogin(userLogin);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (StringUtils.hasText(loginChange)) {
            user.setLogin(loginChange);
        }

        user = userRepository.save(user);
        return user;
    }

    public void deleteUser(String login) {
        userRepository.deleteByLogin(login);
    }
}
