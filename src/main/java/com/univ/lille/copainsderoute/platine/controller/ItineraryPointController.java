package com.univ.lille.copainsderoute.platine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.univ.lille.copainsderoute.platine.service.ItineraryPointService;

import lombok.AllArgsConstructor;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.ItineraryPointRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.ItineraryPoint;
import java.util.List;

@RestController
@RequestMapping("/itinerarypoint")
@AllArgsConstructor
public class ItineraryPointController {
    

    private final ItineraryPointService itineraryPointService;

    @GetMapping("")
    public ResponseEntity<List<ItineraryPoint>> getItineraries() throws RuntimeException{
        List<ItineraryPoint> itineraries = itineraryPointService.getPoint(); 
        if (itineraries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itineraries);
    }

    @PostMapping("")
    public ResponseEntity<ItineraryPoint> createPoint(@RequestBody ItineraryPointRequestDTOs PointRequestDTOs) throws RuntimeException{
        ItineraryPoint Point = itineraryPointService.createPoint(PointRequestDTOs);
        return ResponseEntity.created(null).body(Point);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<ItineraryPoint>> getPoint(@PathVariable("eventId") int id) throws RuntimeException{
        List<ItineraryPoint> Point = itineraryPointService.getPointByEvent(id);
        return ResponseEntity.ok(Point);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ItineraryPoint> patchPoint (@RequestBody ItineraryPointRequestDTOs PointRequestDTOs, @PathVariable("id") int id) throws RuntimeException{
        ItineraryPoint Point = itineraryPointService.patchPoint(PointRequestDTOs, id);
        return ResponseEntity.ok(Point);
    }

    public ResponseEntity<ItineraryPoint> deletePoint(@PathVariable("id") int id) throws RuntimeException{
        itineraryPointService.deletePoint(id);
        return ResponseEntity.noContent().build();
    }

    
}
