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
            List<Integer> participatedEvent_id = new ArrayList<>();
            if (!user.getParticipatedEvent().isEmpty()){
                for (int i = 0; i < user.getParticipatedEvent().size(); i++) {
                    participatedEvent_id.add(user.getParticipatedEvent().get(i).getId());
                }
            UserResponseDTOs userResponseDTO = new UserResponseDTOs(user, participatedEvent_id);
            userResponseDTOs.add(userResponseDTO);
        }
            else{
            UserResponseDTOs userResponseDTO = new UserResponseDTOs(user);
            userResponseDTOs.add(userResponseDTO);
            }
        }
        return userResponseDTOs;
    }

    public UserResponseDTOs createUser(UserRequestDTOs userRequestDTO) {
    
        User user = new User();

        user.setLogin(userRequestDTO.getLogin());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());

        userRepository.save(user);

        UserResponseDTOs userResponseDTO = new UserResponseDTOs(user);

        return userResponseDTO;
    }

    public UserResponseDTOs getUser(int id) throws RuntimeException{
        User user = userRepository.findById(id).get();
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!user.getParticipatedEvent().isEmpty()){
            List<Integer> participatedEvent_id = new ArrayList<>();
            for (int i = 0; i < user.getParticipatedEvent().size(); i++) {
                participatedEvent_id.add(user.getParticipatedEvent().get(i).getId()); 
            }
            UserResponseDTOs userResponseDTO = new UserResponseDTOs(user, participatedEvent_id);
            return userResponseDTO;
        
        }
        UserResponseDTOs userResponseDTO = new UserResponseDTOs(user);
        return userResponseDTO;
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
      
        userRepository.save(user.get());
        return user.get();
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
