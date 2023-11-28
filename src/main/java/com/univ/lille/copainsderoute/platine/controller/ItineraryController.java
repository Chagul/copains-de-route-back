package com.univ.lille.copainsderoute.platine.controller;

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
    public ResponseEntity<List<Itinerary>> getItineraries() throws RuntimeException{
        List<Itinerary> itineraries = itineraryService.getItinerary(); 
        if (itineraries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itineraries);
    }

    @PostMapping("")
    public ResponseEntity<Itinerary> createItinerary(@RequestBody ItineraryRequestDTOs itineraryRequestDTOs) throws RuntimeException{
        Itinerary itinerary = itineraryService.createItinerary(itineraryRequestDTOs);
        return ResponseEntity.created(null).body(itinerary);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<Itinerary>> getItinerary(@PathVariable("eventId") int id) throws RuntimeException{
        List<Itinerary> itinerary = itineraryService.getItineraryByEvent(id);
        return ResponseEntity.ok(itinerary);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Itinerary> patchItinerary (@RequestBody ItineraryRequestDTOs itineraryRequestDTOs, @PathVariable("id") int id) throws RuntimeException{
        Itinerary itinerary = itineraryService.patchItinerary(itineraryRequestDTOs, id);
        return ResponseEntity.ok(itinerary);
    }

    public ResponseEntity<Itinerary> deleteItinerary(@PathVariable("id") int id) throws RuntimeException{
        itineraryService.deleteItinerary(id);
        return ResponseEntity.noContent().build();
    }

    
}
