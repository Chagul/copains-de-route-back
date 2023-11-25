package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.univ.lille.copainsderoute.platine.dtos.UserRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.User;


import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserRequestDTOs userRequestDTO) {
    
        User user = new User();

        user.setLogin(userRequestDTO.getLogin());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setNumberEventsParticipated(userRequestDTO.getNumberEventsParticipated());
        user.setNumberEventsCreated(userRequestDTO.getNumberEventsCreated());
        user.setDistanceTraveled(userRequestDTO.getDistanceTraveled());
        user.setCo2_not_emitted(userRequestDTO.getCo2_not_emitted());
        user.setFriends(userRequestDTO.getFriends());
        

        return userRepository.save(user);

    }

    public User getUser(int id) {
        return userRepository.findById(id).get();
    }

    public User updateUser(UserRequestDTOs userRequestDTO, int id) {
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

        if (userRequestDTO.getFriends() != null) {
            user.get().setFriends(userRequestDTO.getFriends());
        }




        userRepository.save(user.get());
        return user.get();
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
