package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.repository.EventRepository;
import com.univ.lille.copainsderoute.platine.repository.PointLatLngRepository;
import com.univ.lille.copainsderoute.platine.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.EventRequestDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.GpsCoordinatesDTOs;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.EventResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.PointLatLng;
import com.univ.lille.copainsderoute.platine.entity.User;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PointLatLngRepository pointLatLngRepository;

    /**
     * get all events
     * @return
     */
    public List<EventResponseDTOs> getEvents() {
        List<Event> events = eventRepository.findAll();
        
        List<EventResponseDTOs> eventResponseDTOs = new ArrayList<>();
        for (Event event : events) {
            eventResponseDTOs.add(new EventResponseDTOs(event));
        }
        return eventResponseDTOs;

    }

    public int createEvent(EventRequestDTOs eventRequestDTO, String login) throws RuntimeException {
        User promoter = userRepository.findByLogin(login);
    
        if (promoter == null) {
            throw new RuntimeException("Promoter not found");
        }
    
        Event evt = new Event();
        evt.setName(eventRequestDTO.getName());
        evt.setDescription(eventRequestDTO.getDescription());
        evt.setVisibility(eventRequestDTO.getVisibility());
        evt.setMaxParticipants(eventRequestDTO.getMaxParticipants());
        evt.setStartDate(eventRequestDTO.getStartDate());
        evt.setStartTime(eventRequestDTO.getStartTime());
        evt.setRoadType1(eventRequestDTO.getRoadType1());
        evt.setRoadType2(eventRequestDTO.getRoadType2());
        evt.setRoadType3(eventRequestDTO.getRoadType3());
        evt.setBikeType1(eventRequestDTO.getBikeType1());
        evt.setBikeType2(eventRequestDTO.getBikeType2());
        evt.setStartAddress(eventRequestDTO.getStartAddress());
        evt.setEndAddress(eventRequestDTO.getEndAddress());
        
        evt.setPromoter(promoter);
        
        evt.setRoute(eventRequestDTO.getRoute());
        evt.setDistance(eventRequestDTO.getDistance());
    
        eventRepository.save(evt);
    
        for (PointLatLng stepDTO : eventRequestDTO.getSteps()) {
            PointLatLng point = new PointLatLng();
            point.setLatitude(stepDTO.getLatitude());
            point.setLongitude(stepDTO.getLongitude());
            point.setRank(stepDTO.getRank());
            point.setEvent(evt);
            pointLatLngRepository.save(point);
            evt.getSteps().add(point);
        }
    
        List<Event> events = promoter.getParticipatedEvent();
        events.add(evt);
        promoter.setParticipatedEvent(events);
        promoter.setNumberEventsCreated(promoter.getNumberEventsCreated() + 1);
        userRepository.save(promoter);
    
        return evt.getId();
    }
    
    
    
    
    public Event updateEvent (EventRequestDTOs eventRequestDTO, int id, String login) throws RuntimeException {

        Optional<Event> evt = eventRepository.findById(id);

        if(evt.isPresent()) {
            User promoter = userRepository.findByLogin(login);
            if (!evt.get().getPromoter().equals(promoter)) {
                throw new RuntimeException("Authenticated user is not the owner of this event");
            }

            if (eventRequestDTO.getName() != null) {
                evt.get().setName(eventRequestDTO.getName());
            }

            if (eventRequestDTO.getDescription() != null) {
                evt.get().setDescription(eventRequestDTO.getDescription());
            }

            if (eventRequestDTO.getVisibility() != null) {
                evt.get().setVisibility(eventRequestDTO.getVisibility());
            }

            if (eventRequestDTO.getMaxParticipants() != 0) {
                evt.get().setMaxParticipants(eventRequestDTO.getMaxParticipants());
            }

            if (eventRequestDTO.getStartDate() != null) {
                evt.get().setStartDate(eventRequestDTO.getStartDate());
            }

            if (eventRequestDTO.getStartTime() != null) {
                evt.get().setStartTime(eventRequestDTO.getStartTime());
            }

            if (eventRequestDTO.getRoadType1() != null) {
                evt.get().setRoadType1(eventRequestDTO.getRoadType1());
            }

            if (eventRequestDTO.getRoadType2() != null) {
                evt.get().setRoadType2(eventRequestDTO.getRoadType2());
            }

            if (eventRequestDTO.getRoadType3() != null) {
                evt.get().setRoadType3(eventRequestDTO.getRoadType3());
            }

            if (eventRequestDTO.getBikeType1() != null) {
                evt.get().setBikeType1(eventRequestDTO.getBikeType1());
            }

            if (eventRequestDTO.getBikeType2() != null) {
                evt.get().setBikeType2(eventRequestDTO.getBikeType2());
            }

            eventRepository.save(evt.get());
            return evt.get();
        }
        throw new RuntimeException("Event not found");
    }

        
    public void deleteEvent(int id, String login) {
        Optional<Event> evt = eventRepository.findById(id);

        if(evt.isPresent()) {
            User promoter = userRepository.findByLogin(login);
            if (!evt.get().getPromoter().equals(promoter)) {
                throw new RuntimeException("Authenticated user is not the owner of this event");
            }
            eventRepository.deleteById(id);
        }
    }

    public EventResponseDTOs getEvent(int id) throws RuntimeException {
        Optional<Event> evt = eventRepository.findById(id);
        if (evt.isEmpty()) {
            throw new RuntimeException("Event not found");
        }
        return new EventResponseDTOs(evt.get());
    }

    public List<Event> getEventsByLocation(GpsCoordinatesDTOs gpsCoordinatesDTOs) {

        List<Event> events = eventRepository.findByStartDate(LocalDate.now());
        List<Event> eventsByLocation = new ArrayList<>();

        if (events.isEmpty()) {
            throw new RuntimeException("No event today");
        }
        for (Event event : events) {

            PointLatLng point = eventRepository.findById(event.getId()).get().getSteps().get(0);

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

    public void participate(int id, String userLogin) throws RuntimeException {
        Optional<Event> event = eventRepository.findById(id);
        User user = userRepository.findByLogin(userLogin);

        if (event.isEmpty()) {
            throw new RuntimeException("Event not found");
        }

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if(event.get().getMaxParticipants() == event.get().getParticipants().size()) {
            throw new RuntimeException("Maximum number of participants reached for this event");
        }

        long countIfUserIsInEvent = event.get().getParticipants().stream().filter(u -> u.equals(user)).count();
        if(countIfUserIsInEvent == 0) {
            // Ajoutez l'utilisateur à l'événement
            event.get().getParticipants().add(user);

            // Mettez à jour l'événement dans la base de données
            eventRepository.save(event.get());

            // Associez l'utilisateur à l'événement
            List<Event> events = user.getParticipatedEvent();
            events.add(event.get());
            user.setParticipatedEvent(events);
            user.setNumberEventsParticipated(user.getNumberEventsParticipated() + 1);

            // Mettez à jour l'utilisateur dans la base de données
            userRepository.save(user);
        }
        throw new RuntimeException("User is already participating to this event");
    }


    public List<EventResponseDTOs> getEventsByUser(String login) throws RuntimeException {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<Event> events = eventRepository.findByPromoter(user);
        return events.stream().map(EventResponseDTOs::new).toList();
    }

    public List<EventResponseDTOs> getEventsByUserParticipated(String login) throws RuntimeException {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.getNumberEventsParticipated() == 0) {
            throw new RuntimeException("User has not participated to any event");
        }
        List<Event> events = eventRepository.findByParticipants(user);
        return events.stream().map(EventResponseDTOs::new).toList();

    }
}
