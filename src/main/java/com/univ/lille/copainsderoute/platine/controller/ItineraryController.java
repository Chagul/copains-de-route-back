package com.univ.lille.copainsderoute.platine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.univ.lille.copainsderoute.platine.service.ItineraryService;

import lombok.AllArgsConstructor;

import com.univ.lille.copainsderoute.platine.dtos.ItineraryRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.Itinerary;
import java.util.List;

@RestController
@RequestMapping("/itinerary")
@AllArgsConstructor
public class ItineraryController {
    

    private final ItineraryService itineraryService;

    @GetMapping("")
    public ResponseEntity<List<Itinerary>> getItineraries() throws Exception{
        List<Itinerary> itineraries = itineraryService.getItinerary(); 
        if (itineraries.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(itineraries, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Itinerary> createItinerary(@RequestBody ItineraryRequestDTOs itineraryRequestDTOs) throws Exception{
        Itinerary itinerary = itineraryService.createItinerary(itineraryRequestDTOs);
        return new ResponseEntity<>(itinerary, HttpStatus.CREATED);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<Itinerary>> getItinerary(@PathVariable("eventId") int id) throws Exception{
        List<Itinerary> itinerary = itineraryService.getItineraryByEvent(id);
        return new ResponseEntity<>(itinerary, HttpStatus.OK);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Itinerary> patchItinerary (@RequestBody ItineraryRequestDTOs itineraryRequestDTOs, @PathVariable("id") int id) throws Exception{
        Itinerary itinerary = itineraryService.patchItinerary(itineraryRequestDTOs, id);
        return new ResponseEntity<>(itinerary, HttpStatus.OK);
    }

    public ResponseEntity<Itinerary> deleteItinerary(@PathVariable("id") int id) throws Exception{
        itineraryService.deleteItinerary(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    
}
