package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.FriendsRequestResponseDTO;
import com.univ.lille.copainsderoute.platine.entity.Friends;
import com.univ.lille.copainsderoute.platine.exceptions.PasswordsDontMatchException;
import com.univ.lille.copainsderoute.platine.exceptions.ProfilePicNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.TokenExpiredException;
import com.univ.lille.copainsderoute.platine.exceptions.TokenNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.UserNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.UserWithNoProfilePicException;
import com.univ.lille.copainsderoute.platine.exceptions.ZeroUserFoundException;
import com.univ.lille.copainsderoute.platine.repository.UserRepository;
import com.univ.lille.copainsderoute.platine.repository.UserTokenRepository;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.*;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.UserResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.entity.UserToken;

import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private UserTokenRepository userTokenRepository;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender javaMailSender;

    private static final String USER_PATH = "/users/";
    private static final String PROFILE_PIC_PATH = "/profilePic";

    public List<UserResponseDTOs> getUsers() throws ZeroUserFoundException {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ZeroUserFoundException();
        }
        List<UserResponseDTOs> userResponseDTOs = new ArrayList<>();
        for (User user : users) {
            UserResponseDTOs userResponseDTO = new UserResponseDTOs(user, getUserProfilePicLocation(user),
                    createFriendList(user.getSentFriends()), createFriendList(user.getAddedFriends()));
            userResponseDTOs.add(userResponseDTO);
        }
        return userResponseDTOs;
    }

    public User createUser(UserRegisterRequestDTOs userRequestDTO) throws IOException {
        User user = User.getUserFromDTO(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user = userRepository.save(user);
        if (StringUtils.hasText(userRequestDTO.getBase64ProfilePic())) {
            byte[] profilePicBytes = Base64.getDecoder().decode(userRequestDTO.getBase64ProfilePic());
            Files.createDirectories(Paths.get(getProfilePicPath(user.getId())));
            Path destinationFile = Paths.get(getProfilePicPath(user.getId()),
                    "profilePic" + userRequestDTO.getProfilePicFormat());
            Files.write(destinationFile, profilePicBytes);
        }
        return user;
    }

    public UserResponseDTOs getUserByLogin(String login) throws UserNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        return new UserResponseDTOs(user, getUserProfilePicLocation(user),
                createFriendList(user.getSentFriends()), createFriendList(user.getAddedFriends()));
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

    public InputStreamResource getProfilePic(int userId)
            throws ProfilePicNotFoundException, FileNotFoundException, UserWithNoProfilePicException {
        return new InputStreamResource(new FileInputStream(findUserProfilePic(userId).toFile()));
    }

    private String getProfilePicPath(int userId) {
        return "./users/".concat(String.valueOf(userId));
    }

    private Path findUserProfilePic(int id) throws ProfilePicNotFoundException, UserWithNoProfilePicException {
        try (Stream<Path> stream = Files.list(Paths.get(getProfilePicPath(id)))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .findFirst().orElseThrow(ProfilePicNotFoundException::new);
        } catch (IOException e) {
            throw new UserWithNoProfilePicException();
        }
    }

    private boolean userWithProfilePic(int userId) {
        try {
            findUserProfilePic(userId);
        } catch (UserWithNoProfilePicException | ProfilePicNotFoundException e) {
            return false;
        }
        return true;
    }

    public String getUserProfilePicLocation(User user) {
        if (userWithProfilePic(user.getId())) {
            return USER_PATH.concat(String.valueOf(user.getId())).concat(PROFILE_PIC_PATH);
        }
        return null;
    }

    public List<FriendsRequestResponseDTO> createFriendList(List<Friends> friends) {
        return friends.stream().map(f -> new FriendsRequestResponseDTO(f, getUserProfilePicLocation(f.getSender()),
                getUserProfilePicLocation(f.getAdded()))).toList();
    }

    public void resetPassword(String email, String newPassword) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void sendEmail(String to, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ksrnass1208@gmail.com");
        message.setTo(to);
        message.setSubject("Reset Password");
        message.setText(body);

        javaMailSender.send(message);
    }

    public void initiatePasswordReset(String email) {

        String token = UUID.randomUUID().toString();
        UserToken userToken = new UserToken();
        userToken.setExpiryDate(LocalDateTime.now().plusMinutes(60));
        userToken.setToken(token);
        userToken.setUser(email);
        userTokenRepository.save(userToken);

        String resetLink = "http://localhost:8080/users/reset-password?token=" + token;
        sendEmail(email, resetLink);

    }

    public void resetPassword(String token, String password, String confirmPassword)
            throws PasswordsDontMatchException, UserNotFoundException, TokenExpiredException, TokenNotFoundException {
        UserToken userToken = userTokenRepository.findByToken(token);
        if (userToken == null) {
            throw new TokenNotFoundException();
        }
        if (LocalDateTime.now().isAfter(userToken.getExpiryDate())) {
            throw new TokenExpiredException();
        } else {
            User user = userRepository.findByEmail(userToken.getUser());

            if (user != null) {
                if (password.equals(confirmPassword)) {
                    user.setPassword(passwordEncoder.encode(password));
                    userRepository.save(user);
                } else {
                    throw new PasswordsDontMatchException();
                }
            } else {
                throw new UserNotFoundException();
            }
        }
    }

}
