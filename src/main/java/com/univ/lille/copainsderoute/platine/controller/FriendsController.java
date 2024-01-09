package com.univ.lille.copainsderoute.platine.controller;

import com.univ.lille.copainsderoute.platine.authent.JwtUtil;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.FriendsRequestResponseDTO;
import com.univ.lille.copainsderoute.platine.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.FriendsRequestDTOs;
import com.univ.lille.copainsderoute.platine.service.FriendsService;


import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("friends")
@AllArgsConstructor
public class FriendsController {

    private JwtUtil jwtUtil;
    private FriendsService friendsService;

    @PostMapping("add")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendsRequestDTOs friendsRequestDTOs, HttpServletRequest request) {
        try {
            friendsService.sendFriendRequest(friendsRequestDTOs, jwtUtil.getLogin(request));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserCannotAddHimselfAsFriendException e) {
            return ResponseEntity.badRequest().build();
        }catch (FriendRequestAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("pendingRequest")
    public ResponseEntity<List<FriendsRequestResponseDTO>> getFriendRequest(HttpServletRequest request) {
        List<FriendsRequestResponseDTO> friendRequests = new ArrayList<>();
        try {
            friendRequests = friendsService.getFriendRequests(jwtUtil.getLogin(request));
        } catch (UserNotFoundException | NoFriendRequestsException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(friendRequests);
    }

    @PostMapping("accept/{id}")
    public ResponseEntity<?> acceptFriendRequest(HttpServletRequest request, @PathVariable("id") int id) {
        try {
            friendsService.acceptFriendRequest(id, jwtUtil.getLogin(request));
        } catch (UserNotFoundException | FriendRequestNotFound e) {
            return ResponseEntity.notFound().build();
        } catch (UserCanOnlyAcceptHisFriendRequestException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (FriendRequestIsNotInStatusSentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("deny/{id}")
    public ResponseEntity<?> denyFriendRequest(HttpServletRequest request, @PathVariable("id") int id) {
        try {
            friendsService.denyFriendRequest(id, jwtUtil.getLogin(request));
        } catch (UserNotFoundException | FriendRequestNotFound e) {
            return ResponseEntity.notFound().build();
        } catch (UserCanOnlyAcceptHisFriendRequestException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (FriendRequestIsNotInStatusSentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteFriend(HttpServletRequest request, @PathVariable("id") int id) {
        try {
            friendsService.deleteFriend(id, jwtUtil.getLogin(request));
        } catch (UserNotFoundException | FriendRequestNotFound e) {
            return ResponseEntity.notFound().build();
        } catch (UserCanDeleteOnlyHisFriendsException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
