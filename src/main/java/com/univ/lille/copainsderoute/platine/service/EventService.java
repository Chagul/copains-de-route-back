package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.FilterEventRequestDto;
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
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
    public List<EventResponseDTOs> getEvents() throws ZeroEventFoundException {
        List<Event> events = eventRepository.findAll();
        if(events.isEmpty()) {
            throw new ZeroEventFoundException();
        }
        
        List<EventResponseDTOs> eventResponseDTOs = new ArrayList<>();
        for (Event event : events) {
            eventResponseDTOs.add(new EventResponseDTOs(event, userService.getUserProfilePicLocation(event.getPromoter())));
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
        return new EventResponseDTOs(evt, userService.getUserProfilePicLocation(evt.getPromoter()));
    }

    public List<EventResponseDTOs> getEventsByLocation(GpsCoordinatesDTOs gpsCoordinatesDTOs) throws ZeroEventFoundException, EventNotfoundException {
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
        List<EventResponseDTOs> eventResp = eventsByLocation.stream().map(event -> new EventResponseDTOs(event, userService.getUserProfilePicLocation(event.getPromoter()))).toList();
        return removeFullEvents(eventResp);
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
        return events.stream().map(event -> new EventResponseDTOs(event, userService.getUserProfilePicLocation(event.getPromoter()))).toList();
    }

    public List<EventResponseDTOs> getEventsByUserParticipated(String login) throws UserNotFoundException, UserNotParticipatingToAnyEventException {
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

        if (user.getNumberEventsParticipated() == 0) {
            throw new UserNotParticipatingToAnyEventException();
        }
        List<Event> events = user.getParticipatedEvent();
        return events.stream().map(event -> new EventResponseDTOs(event, userService.getUserProfilePicLocation(event.getPromoter()))).toList();
    }

    private List<EventResponseDTOs> removeFullEvents(List<EventResponseDTOs> events) {
        return events.stream().filter(Predicate.not(EventResponseDTOs::isFull)).toList();
    }

    public List<EventResponseDTOs> getEventsByFilters(FilterEventRequestDto filterEventRequestDto, String login) throws UserNotFoundException, ZeroEventFoundException{
        User user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

        List<Event> events = eventRepository.findByFilter(filterEventRequestDto.getStartDate(), filterEventRequestDto.getEndDate(), filterEventRequestDto.getDistanceMin(), filterEventRequestDto.getDistanceMax(), filterEventRequestDto.getVisibility(), filterEventRequestDto.getRoadTypes(), filterEventRequestDto.getBikeTypes());

        List<Event> eventsFilteredVisibility = new ArrayList<>();
        List<Friends> friends = Stream.concat(user.getSentFriends().stream(), user.getAddedFriends().stream()).toList();

        for(Event e : events){
            if(e.getVisibility() == Visibility.PUBLIC || e.getPromoter().getId() == user.getId()) {
                eventsFilteredVisibility.add(e);
            } else {
                for (Friends friend : friends) {
                    if (friend.getSender().equals(e.getPromoter()) || friend.getAdded().equals(e.getPromoter())) {
                        eventsFilteredVisibility.add(e);
                    }
                }
            }
        }

        if(eventsFilteredVisibility.isEmpty()){
            throw new ZeroEventFoundException();
        }
        return eventsFilteredVisibility.stream().map(event -> new EventResponseDTOs(event, userService.getUserProfilePicLocation(event.getPromoter()))).toList();
    }
}
