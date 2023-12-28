package com.univ.lille.copainsderoute.platine.controller;

import com.univ.lille.copainsderoute.platine.authent.JwtUtil;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.FilterEventRequestDto;
import com.univ.lille.copainsderoute.platine.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
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

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.EventRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.GpsCoordinatesDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.EventResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.service.EventService;

import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("events")
@AllArgsConstructor

public class EventController {
    
    private final EventService eventService;
    private final JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity<List<EventResponseDTOs>> getEvents() {
        List<EventResponseDTOs> events = null;
        try {
            events = eventService.getEvents();
        } catch (ZeroEventFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("{id}")
    public ResponseEntity<EventResponseDTOs> getEvent(@PathVariable("id") int id) {
        EventResponseDTOs event = null;
        try {
            event = eventService.getEvent(id);
        } catch (EventNotfoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(event);
    }

    @PostMapping("")
    public ResponseEntity<?> createEvent(HttpServletRequest request, @RequestBody EventRequestDTOs eventRequestDTO) {
        int eventId = 0;
        try {
            eventId = eventService.createEvent(eventRequestDTO, jwtUtil.getLogin(request));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.created(URI.create("/events/" + eventId)).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Event> updateEvent(HttpServletRequest request, @PathVariable("id") int eventId, @RequestBody EventRequestDTOs eventRequestDTO) {
        Event event = null;
        try {
            event = eventService.updateEvent(eventRequestDTO, eventId, jwtUtil.getLogin(request));
        } catch (UserIsNotOwnerOfEventException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EventNotfoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteEvent(HttpServletRequest request, @PathVariable("id") int id) {
        try {
            eventService.deleteEvent(id, jwtUtil.getLogin(request));
        } catch (UserIsNotOwnerOfEventException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EventNotfoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
    

    @GetMapping("location")
    public ResponseEntity<List<EventResponseDTOs>> getEventsByLocation(@RequestBody GpsCoordinatesDTOs gpsCoordinatesDTO) {
        List<EventResponseDTOs> itinerary = null;
        try {
            itinerary = eventService.getEventsByLocation(gpsCoordinatesDTO);
        } catch (ZeroEventFoundException | EventNotfoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itinerary);
    }

    @PostMapping("participate/{id}")
    public ResponseEntity<String> participate(HttpServletRequest request, @PathVariable("id") int id) {
        try {
            eventService.participate(id, jwtUtil.getLogin(request));
        } catch (EventNotfoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserAlreadyParticipatingException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("discard/{id}")
    public ResponseEntity<String> discard(HttpServletRequest request, @PathVariable("id") int id) {
        try {
            eventService.discard(id, jwtUtil.getLogin(request));
        } catch (EventNotfoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserNotParticipatingToEventException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("createdEvents")
    public ResponseEntity<List<EventResponseDTOs>> getUsersByEvent(HttpServletRequest request) {
        List<EventResponseDTOs> events = null;
        try {
            events = eventService.getEventsByUser(jwtUtil.getLogin(request));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("participatedEvents")
    public ResponseEntity<List<EventResponseDTOs>> getParticipatedEvents(HttpServletRequest request) {
        List<EventResponseDTOs> events = null;
        try {
            events = eventService.getEventsByUserParticipated(jwtUtil.getLogin(request));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserNotParticipatingToAnyEventException e) {
            return ResponseEntity.noContent().build();
        }

        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    @PostMapping("eventsByFilter")
    public ResponseEntity<List<EventResponseDTOs>> getEventsByFilters(HttpServletRequest request, @RequestBody FilterEventRequestDto filterEventRequestDto){
        List<EventResponseDTOs> events = null;
        try {
            events = eventService.getEventsByFilters(filterEventRequestDto, jwtUtil.getLogin(request));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ZeroEventFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(events);
    }

}
