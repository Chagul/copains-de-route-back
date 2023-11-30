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

import com.univ.lille.copainsderoute.platine.dtos.EventRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.GpsCoordinatesDTOs;
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
    public ResponseEntity<List<Event>> getEvents() throws RuntimeException{
        List<Event> events = eventService.getEvents();
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("{id}")
    public ResponseEntity<Event> getEvent(@PathVariable("id") int id) throws RuntimeException{
        Event event = eventService.getEvent(id);
        if (event == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(event);
    }

    @PostMapping("")
    public ResponseEntity<Event> createEvent(@RequestBody EventRequestDTOs eventRequestDTO) throws RuntimeException{
        Event event = eventService.createEvent(eventRequestDTO);
        return ResponseEntity.created(null).body(event);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") int id, @RequestBody EventRequestDTOs eventRequestDTO) throws RuntimeException{
        Event event = eventService.updateEvent(eventRequestDTO, id);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") int id) throws RuntimeException{
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted");
    }
    
   @GetMapping("{northeastLatitude}/{northeastLongitude}/{southwestLatitude}/{southwestLongitude}/{northwestLatitude}/{northwestLongitude}/{southeastLatitude}/{southeastLongitude}")
    public ResponseEntity<List<Event>> getEventsByLocation(
            @PathVariable("northeastLatitude") Double northeastLatitude,
            @PathVariable("northeastLongitude") Double northeastLongitude,
            @PathVariable("southwestLatitude") Double southwestLatitude,
            @PathVariable("southwestLongitude") Double southwestLongitude,
            @PathVariable("northwestLatitude") Double northwestLatitude,
            @PathVariable("northwestLongitude") Double northwestLongitude,
            @PathVariable("southeastLatitude") Double southeastLatitude,
            @PathVariable("southeastLongitude") Double southeastLongitude
    ) throws RuntimeException {

        GpsCoordinatesDTOs gpsCoordinatesDTOs = new GpsCoordinatesDTOs(northeastLatitude, northeastLongitude, southwestLatitude, southwestLongitude, northwestLatitude, northwestLongitude, southeastLatitude, southeastLongitude);
        List<Event> itinerary = eventService.getEventsByLocation(gpsCoordinatesDTOs);
        if (itinerary == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itinerary);
    }
    }

