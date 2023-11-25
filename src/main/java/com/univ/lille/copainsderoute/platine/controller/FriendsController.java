package com.univ.lille.copainsderoute.platine.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.univ.lille.copainsderoute.platine.dtos.FriendsRequestDTOs;
import com.univ.lille.copainsderoute.platine.enums.FriendRequestStatus;
import com.univ.lille.copainsderoute.platine.service.FriendsService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/friends")
@AllArgsConstructor
public class FriendsController {

    private FriendsService friendsService;

    @PostMapping()
    public FriendRequestStatus sendFriendRequest (@RequestBody FriendsRequestDTOs friendsRequestDTOs) throws Exception {
        return friendsService.sendFriendRequest(friendsRequestDTOs);
    }
}
