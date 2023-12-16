package com.univ.lille.copainsderoute.platine.controller;

import com.univ.lille.copainsderoute.platine.authent.JwtUtil;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.UserRegisterRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.ChangeLoginUserResponseDTO;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.LoginResponseDTO;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.UserResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.service.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
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
import java.util.List;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity<List<UserResponseDTOs>> getUsers() throws RuntimeException{
        List<UserResponseDTOs> users = userService.getUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("me")
    public ResponseEntity<UserResponseDTOs> getUser(HttpServletRequest request) throws RuntimeException{
        UserResponseDTOs user = userService.getUserByLogin(jwtUtil.getLogin(request));
        return ResponseEntity.ok(user);
    }

    @PatchMapping("me")
    public ResponseEntity<ChangeLoginUserResponseDTO> updateUser(HttpServletRequest request, @RequestParam(value = "login", required = true) String newLogin) throws RuntimeException{
        User user = userService.updateUser(newLogin, jwtUtil.getLogin(request));
        String newToken = jwtUtil.createToken(user);
        return ResponseEntity.ok(new ChangeLoginUserResponseDTO(new UserResponseDTOs(user), new LoginResponseDTO(newToken)));
    }

    @DeleteMapping("me")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) throws RuntimeException{
        userService.deleteUser(jwtUtil.getLogin(request));
        return ResponseEntity.ok(null);
    }

}