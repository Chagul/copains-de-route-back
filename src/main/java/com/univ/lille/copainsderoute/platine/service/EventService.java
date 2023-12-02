package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.repository.EventRepository;
import com.univ.lille.copainsderoute.platine.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import com.univ.lille.copainsderoute.platine.dtos.EventRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.GpsCoordinatesDTOs;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.ItineraryPoint;
import com.univ.lille.copainsderoute.platine.entity.User;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    /**
     * get all events
     * @return
     */
    public List<Event> getEvents() {
        return eventRepository.findByStartDateGreaterThanEqual(LocalDate.now());
    }

    /**
     * create an event
     * @return id of the event
     */
    public Event createEvent(EventRequestDTOs eventRequestDTO) throws RuntimeException{

        User promoter = userRepository.findById(eventRequestDTO.getPromoter()).get();
        if (promoter == null) {
            throw new RuntimeException ("Promoter not found");
        }
        Event evt = new Event();

        evt.setName(eventRequestDTO.getName());
        evt.setDescription(eventRequestDTO.getDescription());
        evt.setVisibility(eventRequestDTO.getVisibility());
        evt.setMaxParticipants (eventRequestDTO.getMaxParticipants());
        evt.setStartDate(eventRequestDTO.getStartDate());
        evt.setStartTime(eventRequestDTO.getStartTime());
        evt.setRoadType1 (eventRequestDTO.getRoadType1());
        evt.setRoadType2(eventRequestDTO.getRoadType2());
        evt.setRoadType3 (eventRequestDTO.getRoadType3());
        evt.setBikeType1(eventRequestDTO.getBikeType1());
        evt.setBikeType2(eventRequestDTO.getBikeType2());
        evt.setStartPoint(eventRequestDTO.getStartPoint());
        evt.setEndPoint(eventRequestDTO.getEndPoint());
        evt.setPromoter(promoter);
        

        eventRepository.save(evt);
        return evt;
    
    }

        public Event updateEvent (EventRequestDTOs eventRequestDTO, int id) throws RuntimeException {

            Event evt = eventRepository.findById(id).get();

            if (evt == null) {
                throw new RuntimeException("Event not found");
            }

            if (eventRequestDTO.getName() != null) {
                evt.setName(eventRequestDTO.getName());
            }

            if (eventRequestDTO.getDescription() != null) {
                evt.setDescription(eventRequestDTO.getDescription());
            }

            if (eventRequestDTO.getVisibility() != null) {
                evt.setVisibility(eventRequestDTO.getVisibility());
            }

            if (eventRequestDTO.getMaxParticipants() != 0) {
                evt.setMaxParticipants(eventRequestDTO.getMaxParticipants());
            }

            if (eventRequestDTO.getStartDate() != null) {
                evt.setStartDate(eventRequestDTO.getStartDate());
            }

            if (eventRequestDTO.getStartTime() != null) {
                evt.setStartTime(eventRequestDTO.getStartTime());
            }

            if (eventRequestDTO.getRoadType1() != null) {
                evt.setRoadType1(eventRequestDTO.getRoadType1());
            }

            if (eventRequestDTO.getRoadType2() != null) {
                evt.setRoadType2(eventRequestDTO.getRoadType2());
            }

            if (eventRequestDTO.getRoadType3() != null) {
                evt.setRoadType3(eventRequestDTO.getRoadType3());
            }

            if (eventRequestDTO.getBikeType1() != null) {
                evt.setBikeType1(eventRequestDTO.getBikeType1());
            }

            if (eventRequestDTO.getBikeType2() != null) {
                evt.setBikeType2(eventRequestDTO.getBikeType2());
            }

            if (eventRequestDTO.getStartPoint() != null) {
                evt.setStartPoint(eventRequestDTO.getStartPoint());
            }

            if (eventRequestDTO.getEndPoint() != null) {
                evt.setEndPoint(eventRequestDTO.getEndPoint());
            }


            eventRepository.save(evt);
            return evt; 
        }

        
        public void deleteEvent(int id) {
            eventRepository.deleteById(id);
        }

        public Event getEvent(int id) throws RuntimeException {
            Optional<Event> evt = eventRepository.findById(id);
            if (evt == null) {
                throw new RuntimeException("Event not found");
            }
            return evt.get();
        }

        public List<Event> getEventsByLocation(GpsCoordinatesDTOs gpsCoordinatesDTOs) {

            List<Event> events = eventRepository.findByStartDate(LocalDate.now());
            List<Event> eventsByLocation = new ArrayList<>();

            for (Event event : events) {

                List<ItineraryPoint> itineraries = eventRepository.findById(event.getId()).get().getItineraryPoints();
                Collections.sort(itineraries, Comparator.comparingInt(ItineraryPoint::getRank));
                ItineraryPoint point = itineraries.get(0);

                boolean isInside = this.isInside(gpsCoordinatesDTOs, point.getLatitude(), point.getLongitude());
                if (isInside) {
                    eventsByLocation.add(event);
                }
            }
                return eventsByLocation;      
        }

        public boolean isInside(GpsCoordinatesDTOs gpsCoordinatesDTOs, Double latitude, Double longitude) {
            
            // Création des points du "rectangle"
            Coordinate northeast = new Coordinate(gpsCoordinatesDTOs.getNortheastLatitude(), gpsCoordinatesDTOs.getNortheastLongitude());
            Coordinate southwest = new Coordinate(gpsCoordinatesDTOs.getSouthwestLatitude(), gpsCoordinatesDTOs.getSouthwestLongitude());
            Coordinate northwest = new Coordinate(gpsCoordinatesDTOs.getNorthwestLatitude(), gpsCoordinatesDTOs.getNorthwestLongitude());
            Coordinate southeast = new Coordinate(gpsCoordinatesDTOs.getSoutheastLatitude(), gpsCoordinatesDTOs.getSoutheastLongitude());

            // Création du point lambda
            Coordinate lambdaCoord = new Coordinate(latitude, longitude);

            GeometryFactory geometryFactory = new GeometryFactory();
            Coordinate[] coordinates = { northeast, northwest, southwest, southeast, northeast };
            Polygon rectangle = geometryFactory.createPolygon(coordinates);

            Point lambdaPoint = geometryFactory.createPoint(lambdaCoord);

            return rectangle.contains(lambdaPoint);
            
        }


}
