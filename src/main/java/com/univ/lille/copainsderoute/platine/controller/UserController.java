package com.univ.lille.copainsderoute.platine.controller;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.UserRegisterRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.UserResponseDTOs;
import com.univ.lille.copainsderoute.platine.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserResponseDTOs>> getUsers() throws RuntimeException{
        List<UserResponseDTOs> users = userService.getUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTOs> getUser(@PathVariable("id") int id) throws RuntimeException{
        UserResponseDTOs user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable("user_id") int id, @RequestBody UserRegisterRequestDTOs userRegisterRequestDTOs) throws RuntimeException{
        userService.updateUser(userRegisterRequestDTOs, id);
        return ResponseEntity.ok("User updated");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id) throws RuntimeException{
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

}