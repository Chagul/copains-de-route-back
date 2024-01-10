package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.FriendsRequestResponseDTO;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.Friends;
import com.univ.lille.copainsderoute.platine.exceptions.PasswordsDontMatchException;
import com.univ.lille.copainsderoute.platine.exceptions.ProfilePicNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.TokenExpiredException;
import com.univ.lille.copainsderoute.platine.exceptions.TokenNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.UserNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.UserWithNoProfilePicException;
import com.univ.lille.copainsderoute.platine.exceptions.ZeroUserFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.LoginAlreadyExistsException;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    private static final double CO2_EMISSION_OF_CAR_PER_KILOMETER = 97d;

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
        if (StringUtils.hasText(userRequestDTO.getBase64ProfilePic()) && StringUtils.hasText(userRequestDTO.getProfilePicFormat())) {
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

        updateDistanceTravelledAndCo2NotEmitted(user);
        return new UserResponseDTOs(user, getUserProfilePicLocation(user),
                createFriendList(user.getSentFriends()), createFriendList(user.getAddedFriends()));
    }

    private void updateDistanceTravelledAndCo2NotEmitted(User user) {
        long userDistanceTravelled = user.getParticipatedEvent().stream().filter(Event::hasPassed).map(Event::getDistance).reduce(0, Integer::sum);
        BigDecimal co2EmittedByCar = BigDecimal.valueOf(userDistanceTravelled)
                .multiply(BigDecimal.valueOf(CO2_EMISSION_OF_CAR_PER_KILOMETER))
                .setScale(0, RoundingMode.HALF_UP);
        // we consider that the emissions of a bike is only 10% of what a car emits per kilometer
        int co2NotEmittedWithBike = co2EmittedByCar.subtract(co2EmittedByCar.divide(BigDecimal.TEN, RoundingMode.HALF_UP)).intValue();

        if(user.getDistanceTraveled() != Math.toIntExact(userDistanceTravelled)) {
            user.setDistanceTraveled(Math.toIntExact(userDistanceTravelled));
        }
        if(user.getCo2_not_emitted() != co2NotEmittedWithBike) {
            user.setCo2_not_emitted(co2NotEmittedWithBike);
        }
        userRepository.save(user);
    }

    public User updateUser(UserUpdateRequestDTOs updateRequestDTOs, String userLogin) throws UserNotFoundException, PasswordsDontMatchException, LoginAlreadyExistsException, IOException {
        User user = userRepository.findByLogin(userLogin).orElseThrow(UserNotFoundException::new);
        if (StringUtils.hasText(updateRequestDTOs.getLogin())) {
            Optional<User> existing = userRepository.findByLogin(updateRequestDTOs.getLogin());
            if(existing.isPresent()) {
                throw new LoginAlreadyExistsException();
            }
            user.setLogin(updateRequestDTOs.getLogin());
        }
        if (StringUtils.hasText(updateRequestDTOs.getNewPassword()) && StringUtils.hasText(updateRequestDTOs.getOldPassword())){
            if (!passwordEncoder.matches(updateRequestDTOs.getOldPassword(), user.getPassword())) {
                throw new PasswordsDontMatchException();
            } else {
                String userNewPassword = passwordEncoder.encode(updateRequestDTOs.getNewPassword());
                user.setPassword(userNewPassword);
            }
        }
        if (StringUtils.hasText(updateRequestDTOs.getBase64ProfilePic()) && StringUtils.hasText(updateRequestDTOs.getProfilePicFormat())) {
            byte[] profilePicBytes = Base64.getDecoder().decode(updateRequestDTOs.getBase64ProfilePic());
            if(Files.exists(Paths.get(getProfilePicPath(user.getId())))) {
                try(Stream<Path> files = Files.list(Paths.get(getProfilePicPath(user.getId())))) {
                    String fileName = files.map(p -> p.toFile().getName()).findFirst().get();
                    Files.deleteIfExists(Paths.get(getProfilePicPath(user.getId()), fileName));
                }
            }
            Files.createDirectories(Paths.get(getProfilePicPath(user.getId())));
            Path destinationFile = Paths.get(getProfilePicPath(user.getId()),
                    "profilePic" + updateRequestDTOs.getProfilePicFormat());
            Files.write(destinationFile, profilePicBytes);
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
    public void sendEmail(String to, String object, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("copainsderoute@gmail.com");
        message.setTo(to);
        message.setSubject(object);
        message.setText(body);

        javaMailSender.send(message);
    }

    public void initiatePasswordReset(String email, String baseUrl) {

        String token = UUID.randomUUID().toString();
        UserToken userToken = new UserToken();
        userToken.setExpiryDate(LocalDateTime.now().plusMinutes(60));
        userToken.setToken(token);
        userToken.setUser(email);
        userTokenRepository.save(userToken);

        String resetLink = baseUrl.concat("/users/reset-password?token=").concat(token);
        sendEmail(email, "Reset your password",resetLink);

    }

    public void resetPasswordByEmail(String token, String password, String confirmPassword)
            throws PasswordsDontMatchException, UserNotFoundException, TokenExpiredException, TokenNotFoundException {
        UserToken userToken = userTokenRepository.findByToken(token).orElseThrow(TokenNotFoundException::new);

        if (LocalDateTime.now().isAfter(userToken.getExpiryDate())) {
            throw new TokenExpiredException();
        } else {
            User user = userRepository.findByEmail(userToken.getUser()).orElseThrow(UserNotFoundException::new);
          
                if (password.equals(confirmPassword)) {
                    user.setPassword(passwordEncoder.encode(password));
                    userRepository.save(user);
                } else {
                    throw new PasswordsDontMatchException();
                }
            } 
        }
    }

