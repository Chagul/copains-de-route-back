package com.univ.lille.copainsderoute.platine.controller;

import com.univ.lille.copainsderoute.platine.authent.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<EventResponseDTOs>> getEvents() throws RuntimeException{
        List<EventResponseDTOs> events = eventService.getEvents();
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("{id}")
    public ResponseEntity<EventResponseDTOs> getEvent(@PathVariable("id") int id) throws RuntimeException{
        EventResponseDTOs event = eventService.getEvent(id);
        if (event == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(event);
    }

    @PostMapping("")
    public ResponseEntity<?> createEvent(HttpServletRequest request, @RequestBody EventRequestDTOs eventRequestDTO) throws RuntimeException{
        int eventId = eventService.createEvent(eventRequestDTO, jwtUtil.getLogin(request));
        return ResponseEntity.created(URI.create("/events/" + eventId)).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Event> updateEvent(HttpServletRequest request, @PathVariable("id") int id, @RequestBody EventRequestDTOs eventRequestDTO) throws RuntimeException{
        Event event = eventService.updateEvent(eventRequestDTO, id, jwtUtil.getLogin(request));
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteEvent(HttpServletRequest request, @PathVariable("id") int id) throws RuntimeException{
        eventService.deleteEvent(id, jwtUtil.getLogin(request));
        return ResponseEntity.ok(null);
    }
    

    @GetMapping("/location")
    public ResponseEntity<List<Event>> getEventsByLocation(@RequestBody GpsCoordinatesDTOs gpsCoordinatesDTO) throws RuntimeException {
        
        List<Event> itinerary = eventService.getEventsByLocation(gpsCoordinatesDTO);
        if (itinerary == null || itinerary.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itinerary);
    }

    @PostMapping("/participate/{id}")
    public ResponseEntity<String> participate(HttpServletRequest request, @PathVariable("id") int id) throws RuntimeException{
        eventService.participate(id, jwtUtil.getLogin(request));
        return ResponseEntity.ok("User added to the event");    
    }

    @GetMapping("createdEvents")
    public ResponseEntity<List<EventResponseDTOs>> getUsersByEvent(HttpServletRequest request) throws RuntimeException{
        List<EventResponseDTOs> events = eventService.getEventsByUser(jwtUtil.getLogin(request));
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("participatedEvents")
    public ResponseEntity<List<EventResponseDTOs>> getParticipatedEvents(HttpServletRequest request) throws RuntimeException{
        List<EventResponseDTOs> events = eventService.getEventsByUserParticipated(jwtUtil.getLogin(request));
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }
}
