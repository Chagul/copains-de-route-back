package com.univ.lille.copainsderoute.platine.controller;

import com.univ.lille.copainsderoute.platine.authent.JwtUtil;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.LoginRequestDTO;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.UserRegisterRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.LoginResponseDTO;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request)  {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
            String login = authentication.getName();
            User user = new User();
            user.setLogin(login);
            String token = jwtUtil.createToken(user);
            LoginResponseDTO loginRes = new LoginResponseDTO(token);

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("register")
    public ResponseEntity<?> createUser(@RequestBody UserRegisterRequestDTOs userRegisterRequestDTOs){
        User createdUser = userService.createUser(userRegisterRequestDTOs);
        if(createdUser != null)
            return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).build();
        return ResponseEntity.internalServerError().build();
    }
}
