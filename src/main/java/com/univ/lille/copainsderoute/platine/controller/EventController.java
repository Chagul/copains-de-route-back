package com.univ.lille.copainsderoute.platine.controller;

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

import com.univ.lille.copainsderoute.platine.dtos.EventRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.service.EventService;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor

public class EventController {
    
    private final EventService eventService;
    @GetMapping("")
    public ResponseEntity<List<Event>> getEvents() throws Exception{
        List<Event> events = eventService.getEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Event> getEvent(@PathVariable("id") int id) throws Exception{
        Event event = eventService.getEvent(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Event> createEvent(@RequestBody EventRequestDTOs eventRequestDTO) throws Exception{
        Event event = eventService.createEvent(eventRequestDTO);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") int id, @RequestBody EventRequestDTOs eventRequestDTO) throws Exception{
        Event event = eventService.updateEvent(eventRequestDTO, id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") int id) throws Exception{
        eventService.deleteEvent(id);
        return new ResponseEntity<>("Event deleted successfully", HttpStatus.OK);
    }
    
}
