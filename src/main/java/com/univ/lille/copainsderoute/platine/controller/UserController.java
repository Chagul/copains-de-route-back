package com.univ.lille.copainsderoute.platine.controller;

import com.univ.lille.copainsderoute.platine.authent.JwtUtil;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.UserRegisterRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.ChangeLoginUserResponseDTO;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.LoginResponseDTO;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.UserResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.exceptions.ProfilePicNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.UserNotFoundException;
import com.univ.lille.copainsderoute.platine.exceptions.UserWithNoProfilePicException;
import com.univ.lille.copainsderoute.platine.exceptions.ZeroUserFoundException;
import com.univ.lille.copainsderoute.platine.service.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.apache.coyote.Response;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity<List<UserResponseDTOs>> getUsers() throws RuntimeException{
        List<UserResponseDTOs> users = null;
        try {
            users = userService.getUsers();
        } catch (ZeroUserFoundException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("me")
    public ResponseEntity<UserResponseDTOs> getUser(HttpServletRequest request) throws RuntimeException{
        UserResponseDTOs user = null;
        try {
            user = userService.getUserByLogin(jwtUtil.getLogin(request));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PatchMapping("me")
    public ResponseEntity<ChangeLoginUserResponseDTO> updateUser(HttpServletRequest request, @RequestParam(value = "login", required = true) String newLogin) throws RuntimeException{
        User user = null;
        try {
            user = userService.updateUser(newLogin, jwtUtil.getLogin(request));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        String newToken = jwtUtil.createToken(user);
        return ResponseEntity.ok(new ChangeLoginUserResponseDTO(new UserResponseDTOs(user, userService.userWithProfilePic(user.getId())), new LoginResponseDTO(newToken)));
    }

    @DeleteMapping("me")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) throws RuntimeException{
        userService.deleteUser(jwtUtil.getLogin(request));
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{id}/profilePic", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getProfilePic(@PathVariable("id") int userId) {
        InputStreamResource resource = null;
        try {
            resource = userService.getProfilePic(userId);
        } catch (ProfilePicNotFoundException | FileNotFoundException | UserWithNoProfilePicException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resource);
    }
}