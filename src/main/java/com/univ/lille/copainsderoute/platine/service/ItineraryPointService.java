package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.repository.EventRepository;
import com.univ.lille.copainsderoute.platine.repository.ItineraryPointRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.ItineraryPointRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.ItineraryPoint;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.Comparator;

@Service
@AllArgsConstructor
public class ItineraryPointService {

    private ItineraryPointRepository itineraryPointService;
    private EventRepository eventRepository;

    public List<ItineraryPoint> getPoint() throws RuntimeException {
        return itineraryPointService.findAll();
    }

    public ItineraryPoint createPoint(ItineraryPointRequestDTOs pointRequestDTOs) throws RuntimeException {

        int event_id = pointRequestDTOs.getEvent();
        
        Optional<Event> event = eventRepository.findById(event_id);
        if (!event.isPresent()) {
            throw new RuntimeException("Event not found");
        }
      
        ItineraryPoint Point = new ItineraryPoint();
        Point.setEvent(event_id);
        Point.setLatitude(pointRequestDTOs.getLatitude());
        Point.setLongitude(pointRequestDTOs.getLongitude());
        Point.setRank(pointRequestDTOs.getRank());

        event.get().getItineraryPoints().add(Point);
        
        itineraryPointService.save(Point);
        return Point;
    }
    


    public List<ItineraryPoint> getPointByEvent(int id) throws RuntimeException {

        Optional<Event> event = eventRepository.findById(id);
        if (!event.isPresent()) {
            throw new RuntimeException("Event not found");
        }
        List<ItineraryPoint> itineraries = itineraryPointService.findByEvent(id);
        if (itineraries.isEmpty()) {
            throw new RuntimeException("Point not found");
        }

        // Utiliser Comparator.comparingInt() pour trier par le champ rank et avoir la liste trier par ordre croissant
       Collections.sort(itineraries, Comparator.comparingInt(ItineraryPoint::getRank));
    
        return itineraries;
    }
    
    public ItineraryPoint patchPoint(ItineraryPointRequestDTOs PointRequestDTOs, int id) throws RuntimeException {

        Optional<ItineraryPoint> Point = itineraryPointService.findById(id);
        if (!Point.isPresent()) {
            throw new RuntimeException("Point not found");
        }

        if (PointRequestDTOs.getLatitude() != null) {
            Point.get().setLatitude(PointRequestDTOs.getLatitude());
        }
        if (PointRequestDTOs.getLongitude() != null) {
            Point.get().setLongitude(PointRequestDTOs.getLongitude());
        }
        if (PointRequestDTOs.getRank() != 0) {
            Point.get().setRank(PointRequestDTOs.getRank());
        
        }
        return itineraryPointService.save(Point.get());
    }

    public void deletePoint(int id) throws RuntimeException {

        Optional<ItineraryPoint> Point = itineraryPointService.findById(id);
        if (!Point.isPresent()) {
            throw new RuntimeException("Point not found");
        }
        itineraryPointService.delete(Point.get());
    }
}
