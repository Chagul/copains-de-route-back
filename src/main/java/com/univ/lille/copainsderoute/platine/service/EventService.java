package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.FilterEventRequestDto;
import com.univ.lille.copainsderoute.platine.dtos.dtoResponse.UserResponseDTOs;
import com.univ.lille.copainsderoute.platine.entity.Friends;
import com.univ.lille.copainsderoute.platine.enums.Visibility;
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
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PointLatLngRepository pointLatLngRepository;

    /**
     * get all events
     * @return
     */
    public List<EventResponseDTOs> getEvents(String login) throws ZeroEventFoundException, UserNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

        List<Event> events = eventRepository.findByStartDateGreaterThanEqual(LocalDate.now());
        if(events.isEmpty()) {
            throw new ZeroEventFoundException();
        }

        return eventResponseDtoFromEvent(filterEventWithVisibility(user, events));

    }

    private List<EventResponseDTOs> eventResponseDtoFromEvent(List<Event> events) {
        List<EventResponseDTOs> eventResponseDTOs = new ArrayList<>();
        for (Event event : events) {
            List<UserResponseDTOs> participants = new ArrayList<>();
            for (User user : event.getParticipants()){
                UserResponseDTOs userResponseDTOs = new UserResponseDTOs(user, userService.getUserProfilePicLocation(user));
                participants.add(userResponseDTOs);
            }
            eventResponseDTOs.add(new EventResponseDTOs(event, userService.getUserProfilePicLocation(event.getPromoter()), participants));
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
            List<UserResponseDTOs> participants = new ArrayList<>();
            for (User user : evt.getParticipants()) {
                UserResponseDTOs userResponseDTOs = new UserResponseDTOs(user, userService.getUserProfilePicLocation(user));
                participants.add(userResponseDTOs);
            }
        return new EventResponseDTOs(evt, userService.getUserProfilePicLocation(evt.getPromoter()),participants);
    }

    public List<EventResponseDTOs> getEventsByLocation(GpsCoordinatesDTOs gpsCoordinatesDTOs, String login) throws ZeroEventFoundException, EventNotfoundException, UserNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

        List<Event> events = eventRepository.findByStartDateGreaterThanEqual(LocalDate.now());
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
        filterEventWithVisibility(user, events);

        return removeFullEvents(eventResponseDtoFromEvent(filterEventWithVisibility(user, eventsByLocation)));
    }

    private List<Event> filterEventWithVisibility(User user, List<Event> events) {
        List<Event> eventsFilteredByVisibility = new ArrayList<>();

        List<Friends> friends = user.getFriends();
        for (Event e : events) {
            if (e.getVisibility() == Visibility.PUBLIC || e.getPromoter().getId() == user.getId()) {
                eventsFilteredByVisibility.add(e);
            } else {
                for (Friends friend : friends) {
                    if (friend.getUser1().equals(e.getPromoter()) || friend.getUser2().equals(e.getPromoter())) {
                        eventsFilteredByVisibility.add(e);
                    }
                }
            }
        }

        return eventsFilteredByVisibility;
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

        if(!event.isUserInParticipants(user)) {
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
        else {
            throw new UserAlreadyParticipatingException();
        }
    }

    public void discard(int id, String userLogin) throws EventNotfoundException, UserNotFoundException, UserNotParticipatingToEventException {
        Event event = eventRepository.findById(id).orElseThrow(EventNotfoundException::new);
        User user = userRepository.findByLogin(userLogin).orElseThrow(UserNotFoundException::new);

        if(event.isUserInParticipants(user)) {
            event.getParticipants().remove(user);
            eventRepository.save(event);
            user.getParticipatedEvent().remove(event);
            user.setNumberEventsParticipated(user.getNumberEventsParticipated()-1);
            userRepository.save(user);
        } else {
            throw new UserNotParticipatingToEventException();
        }
    }

    public List<EventResponseDTOs> getEventsByUser(String login) throws UserNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

        List<Event> events = eventRepository.findByPromoter(user);
        return eventResponseDtoFromEvent(events);
    }

    public List<EventResponseDTOs> getEventsByUserParticipated(String login) throws UserNotFoundException, UserNotParticipatingToAnyEventException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

        if (user.getNumberEventsParticipated() == 0) {
            throw new UserNotParticipatingToAnyEventException();
        }
        List<Event> events = user.getParticipatedEvent();
        return eventResponseDtoFromEvent(events);
    }

    private List<EventResponseDTOs> removeFullEvents(List<EventResponseDTOs> events) {
        return events.stream().filter(Predicate.not(EventResponseDTOs::isFull)).toList();
    }

    public List<EventResponseDTOs> getEventsByFilters(FilterEventRequestDto filterEventRequestDto, String login) throws UserNotFoundException, ZeroEventFoundException{
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

        List<Event> events = eventRepository.findByFilter(filterEventRequestDto.getStartDate(), filterEventRequestDto.getEndDate(), filterEventRequestDto.getDistanceMin(), filterEventRequestDto.getDistanceMax(), filterEventRequestDto.getVisibility(), filterEventRequestDto.getRoadTypes(), filterEventRequestDto.getBikeTypes());

        List<Event> eventsFilteredVisibility = filterEventWithVisibility(user, events);

        if(eventsFilteredVisibility.isEmpty()){
            throw new ZeroEventFoundException();
        }
        return eventResponseDtoFromEvent(eventsFilteredVisibility);
    }
}
