package com.univ.lille.copainsderoute.platine.controller;

import com.univ.lille.copainsderoute.platine.dtos.UserRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<List<User>> getUsers() throws RuntimeException{
        List<User> users = userService.getUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id) throws RuntimeException{
        User user = userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("")
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTOs userRequestDTOs){
        User createdUser = userService.createUser(userRequestDTOs);
        return ResponseEntity.created(null).body(createdUser.getLogin());
    }

    @PatchMapping("{user_id}")
    public ResponseEntity<User> updateUser(@PathVariable("user_id") int id, @RequestBody UserRequestDTOs userRequestDTOs) throws RuntimeException{
        User updatedUser = userService.updateUser(userRequestDTOs, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id) throws RuntimeException{
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }


    
}
