package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.repository.UserRepository;
import com.univ.lille.copainsderoute.platine.dtos.UserRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.User;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserRequestDTOs userRequestDTO) {
    
        User user = new User(userRequestDTO);

        return userRepository.save(user);

    }

    public User getUser(int id) {
        return userRepository.findById(id).get();
    }

    public User updateUser(UserRequestDTOs userRequestDTO, int id) {
        User user = userRepository.findById(id).get();

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

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
