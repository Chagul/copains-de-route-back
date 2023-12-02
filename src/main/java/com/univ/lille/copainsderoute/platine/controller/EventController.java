package com.univ.lille.copainsderoute.platine.controller;

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

import java.util.List;

@RestController
@RequestMapping("events")
@AllArgsConstructor

public class EventController {
    
    private final EventService eventService;
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
    public ResponseEntity<String> createEvent(@RequestBody EventRequestDTOs eventRequestDTO) throws RuntimeException{
        String eventName = eventService.createEvent(eventRequestDTO);
        return ResponseEntity.created(null).body(eventName);
    }

    @PatchMapping("{id}")
    public ResponseEntity<String> updateEvent(@PathVariable("id") int id, @RequestBody EventRequestDTOs eventRequestDTO) throws RuntimeException{
        eventService.updateEvent(eventRequestDTO, id);
        return ResponseEntity.ok("Event updated");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") int id) throws RuntimeException{
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted");
    }
    

    @GetMapping("/location")
    public ResponseEntity<List<Event>> getEventsByLocation(@RequestBody GpsCoordinatesDTOs gpsCoordinatesDTO) throws RuntimeException {
        
        List<Event> itinerary = eventService.getEventsByLocation(gpsCoordinatesDTO);
        if (itinerary == null || itinerary.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itinerary);
    }
    
    }

