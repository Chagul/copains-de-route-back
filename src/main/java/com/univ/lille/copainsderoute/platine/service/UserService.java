package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.repository.UserRepository;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.*;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.UserResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.User;



import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;


    public List<UserResponseDTOs> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTOs> userResponseDTOs = new ArrayList<>();
        for (User user : users) {
            UserResponseDTOs userResponseDTO = new UserResponseDTOs(user.getLogin());
            userResponseDTOs.add(userResponseDTO);
            }
        return userResponseDTOs;
    }

    public UserResponseDTOs createUser(UserRequestDTOs userRequestDTO) {
    
        User user = new User();

        user.setLogin(userRequestDTO.getLogin());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());

        userRepository.save(user);

        UserResponseDTOs userResponseDTO = new UserResponseDTOs(user.getLogin());

        return userResponseDTO;
    }

    public User getUser(int id) {
        return userRepository.findById(id).get();
    }

    public User updateUser(UserRequestDTOs userRequestDTO, int id) throws RuntimeException{
        
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
        
        if (userRequestDTO.getNumberEventsParticipated() != 0) {
            user.get().setNumberEventsParticipated(userRequestDTO.getNumberEventsParticipated());
        }

        if (userRequestDTO.getNumberEventsCreated() != 0) {
            user.get().setNumberEventsCreated(userRequestDTO.getNumberEventsCreated());
        }

        if (userRequestDTO.getDistanceTraveled() != 0) {
            user.get().setDistanceTraveled(userRequestDTO.getDistanceTraveled());
        }

        if (userRequestDTO.getCo2_not_emitted() != 0) {
            user.get().setCo2_not_emitted(userRequestDTO.getCo2_not_emitted());
        }

        userRepository.save(user.get());
        return user.get();
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
