package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.repository.UserRepository;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.*;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.UserResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.User;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

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

    public User updateUser(UserRegisterRequestDTOs userRequestDTO, int id) throws RuntimeException{
        
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }

        if (userRequestDTO.getLogin() != null) {
            user.get().setLogin(userRequestDTO.getLogin());
        }

        if (userRequestDTO.getEmail() != null) {
            user.get().setEmail(userRequestDTO.getEmail());
        }
         
        if (userRequestDTO.getPassword() != null) {
            user.get().setPassword(userRequestDTO.getPassword());
        }
      
        userRepository.save(user.get());
        return user.get();
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
