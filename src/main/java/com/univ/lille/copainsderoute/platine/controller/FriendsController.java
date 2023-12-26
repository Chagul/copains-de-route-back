package com.univ.lille.copainsderoute.platine.controller;

import com.univ.lille.copainsderoute.platine.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.FriendsRequestDTOs;
import com.univ.lille.copainsderoute.platine.enums.FriendRequestStatus;
import com.univ.lille.copainsderoute.platine.service.FriendsService;


import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/friends")
@AllArgsConstructor
public class FriendsController {

    private FriendsService friendsService;

    @PostMapping()
    public ResponseEntity<?> sendFriendRequest (@RequestBody FriendsRequestDTOs friendsRequestDTOs) {
        try {
            friendsService.sendFriendRequest(friendsRequestDTOs);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
