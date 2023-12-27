package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.exceptions.ProfilePicNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.UserNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.ZeroUserFoundException;
import com.univ.lille.copainsderoute.platine.repository.UserRepository;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.*;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.UserResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.User;


import org.springframework.core.io.InputStreamResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public List<UserResponseDTOs> getUsers() throws ZeroUserFoundException {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) {
            throw new ZeroUserFoundException();
        }
        List<UserResponseDTOs> userResponseDTOs = new ArrayList<>();
        for (User user : users) {
           UserResponseDTOs userResponseDTO = new UserResponseDTOs(user);
           userResponseDTOs.add(userResponseDTO);
        }
        return userResponseDTOs;
    }

    public User createUser(UserRegisterRequestDTOs userRequestDTO) throws IOException {
        User user = User.getUserFromDTO(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user = userRepository.save(user);
        if(StringUtils.hasText(userRequestDTO.getBase64ProfilePic())) {
            byte[] profilePicBytes = Base64.getDecoder().decode(userRequestDTO.getBase64ProfilePic());
            Files.createDirectories(Paths.get(getProfilePicPath(user)));
            Path destinationFile = Paths.get(getProfilePicPath(user), "profilePic" + userRequestDTO.getProfilePicFormat());
            Files.write(destinationFile, profilePicBytes);
        }
        return user;
    }

    public UserResponseDTOs getUserByLogin(String login) throws UserNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        return new UserResponseDTOs(user);
    }

    public User updateUser(String loginChange, String userLogin) throws UserNotFoundException {
        User user = userRepository.findByLogin(userLogin).orElseThrow(UserNotFoundException::new);
        if (StringUtils.hasText(loginChange)) {
            user.setLogin(loginChange);
        }
        user = userRepository.save(user);
        return user;
    }

    public void deleteUser(String login) {
        userRepository.deleteByLogin(login);
    }

    public InputStreamResource getProfilePic(int userId) throws ProfilePicNotFoundException, FileNotFoundException {
        return new InputStreamResource(new FileInputStream(findUserProfilePic(userId).toFile()));
    }

    private String getProfilePicPath(User user) {
        return "./users/".concat(String.valueOf(user.getId()));
    }

    private String getProfilePicPath(int userId) {
        return "./users/".concat(String.valueOf(userId));
    }

    public Path findUserProfilePic(int id) throws ProfilePicNotFoundException {
        try (Stream<Path> stream = Files.list(Paths.get(getProfilePicPath(id)))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .findFirst().orElseThrow(ProfilePicNotFoundException::new);
        } catch (IOException e) {
            return null;
        }
    }
}
