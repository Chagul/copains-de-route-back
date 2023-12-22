package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.exceptions.*;
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
    public List<EventResponseDTOs> getEvents() throws ZeroEventFoundException {
        List<Event> events = eventRepository.findAll();
        if(events.isEmpty()) {
            throw new ZeroEventFoundException();
        }
        
        List<EventResponseDTOs> eventResponseDTOs = new ArrayList<>();
        for (Event event : events) {
            eventResponseDTOs.add(new EventResponseDTOs(event));
        }
        return eventResponseDTOs;

    }

    public int createEvent(EventRequestDTOs eventRequestDTO, String login) throws UserNotFoundException {
        User promoter = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

        Event evt = Event.getEventFromDTO(eventRequestDTO, promoter);

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

    public Event updateEvent (EventRequestDTOs eventRequestDTO, int id, String login) throws UserIsNotOwnerOfEventException, EventNotfoundException, UserNotFoundException {
        Event evt = eventRepository.findById(id).orElseThrow(EventNotfoundException::new);
        User promoter = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        if (!evt.getPromoter().equals(promoter)) {
            throw new UserIsNotOwnerOfEventException();
        }
        evt.updateFromDTO(eventRequestDTO);
        eventRepository.save(evt);
        return evt;
    }

    public void deleteEvent(int id, String login) throws UserIsNotOwnerOfEventException, EventNotfoundException, UserNotFoundException {
        Event evt = eventRepository.findById(id).orElseThrow(EventNotfoundException::new);
        User promoter = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        if (!evt.getPromoter().equals(promoter)) {
            throw new UserIsNotOwnerOfEventException();
        }
        eventRepository.deleteById(id);
    }

    public EventResponseDTOs getEvent(int id) throws EventNotfoundException {
        Event evt = eventRepository.findById(id).orElseThrow(EventNotfoundException::new);
        return new EventResponseDTOs(evt);
    }

    public List<Event> getEventsByLocation(GpsCoordinatesDTOs gpsCoordinatesDTOs) throws ZeroEventFoundException, EventNotfoundException {

        List<Event> events = eventRepository.findByStartDate(LocalDate.now());
        List<Event> eventsByLocation = new ArrayList<>();

        if (events.isEmpty()) {
            throw new ZeroEventFoundException();
        }
        for (Event event : events) {
            PointLatLng point = eventRepository.findById(event.getId()).orElseThrow(EventNotfoundException::new).getSteps().get(0);
            boolean isInside = this.isInside(gpsCoordinatesDTOs, point.getLatitude(), point.getLongitude());
            if (isInside) {
                eventsByLocation.add(event);
            }
        }
        return eventsByLocation;
    }

    private boolean isInside(GpsCoordinatesDTOs gpsCoordinatesDTOs, Double latitude, Double longitude) {

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

    public void participate(int id, String userLogin) throws EventNotfoundException, UserNotFoundException, UserAlreadyParticipatingException {
        Event event = eventRepository.findById(id).orElseThrow(EventNotfoundException::new);
        User user = userRepository.findByLogin(userLogin).orElseThrow(UserNotFoundException::new);

        long countIfUserIsInEvent = event.getParticipants().stream().filter(u -> u.equals(user)).count();
        if(countIfUserIsInEvent == 0) {
            // Ajoutez l'utilisateur à l'événement
            event.getParticipants().add(user);

            // Mettez à jour l'événement dans la base de données
            eventRepository.save(event);

            // Associez l'utilisateur à l'événement
            user.getParticipatedEvent().add(event);
            user.setNumberEventsParticipated(user.getNumberEventsParticipated() + 1);

            // Mettez à jour l'utilisateur dans la base de données
            userRepository.save(user);
        }
        throw new UserAlreadyParticipatingException();
    }


    public List<EventResponseDTOs> getEventsByUser(String login) throws UserNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

        List<Event> events = eventRepository.findByPromoter(user);
        return events.stream().map(EventResponseDTOs::new).toList();
    }

    public List<EventResponseDTOs> getEventsByUserParticipated(String login) throws UserNotFoundException, UserNotParticipatingToAnyEventException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

        if (user.getNumberEventsParticipated() == 0) {
            throw new UserNotParticipatingToAnyEventException();
        }
        List<Event> events = eventRepository.findByParticipants(user);
        return events.stream().map(EventResponseDTOs::new).toList();
    }
}
