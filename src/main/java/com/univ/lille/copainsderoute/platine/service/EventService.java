package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.repository.EventRepository;
import com.univ.lille.copainsderoute.platine.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import com.univ.lille.copainsderoute.platine.dtos.EventRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.User;

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
        return eventRepository.findAll();
    }

    /**
     * create an event
     * @return id of the event
     */
    public Event createEvent(EventRequestDTOs eventRequestDTO) {
        Event evt = new Event();
        User user = userRepository.findById(eventRequestDTO.getPromoter()).get();
        evt.setName(eventRequestDTO.getName());

        evt.setDescription(eventRequestDTO.getDescription());

        evt.setPromoter(user);  

        //evt.setItineraryPoints(eventRequestDTO.getItineraryPoints());
        evt.setMaxParticipants(eventRequestDTO.getMaxParticipants());
        evt.setStartTime(eventRequestDTO.getStartTime());
        evt.setRoadType1(eventRequestDTO.getRoadType1());
        evt.setRoadType2(eventRequestDTO.getRoadType2());
        evt.setRoadType3(eventRequestDTO.getRoadType3());
        evt.setBikeType1(eventRequestDTO.getBikeType1());
        evt.setBikeType2(eventRequestDTO.getBikeType2());
        evt.setStartPoint(eventRequestDTO.getStartPoint());
        evt.setEndPoint(eventRequestDTO.getEndPoint());

        eventRepository.save(evt);
        return evt; }

        public Event updateEvent (EventRequestDTOs eventRequestDTO, int id) {

            Event evt = eventRepository.findById(id).get();
            User user = userRepository.findById(eventRequestDTO.getPromoter()).get();

            evt.setName(eventRequestDTO.getName());
            evt.setDescription(eventRequestDTO.getDescription());

            evt.setPromoter(user);
            
        //evt.setItineraryPoints(eventRequestDTO.getItineraryPoints());
            evt.setMaxParticipants(eventRequestDTO.getMaxParticipants());
            evt.setStartTime(eventRequestDTO.getStartTime());
            evt.setRoadType1(eventRequestDTO.getRoadType1());
            evt.setRoadType2(eventRequestDTO.getRoadType2());
            evt.setRoadType3(eventRequestDTO.getRoadType3());
            evt.setBikeType1(eventRequestDTO.getBikeType1());
            evt.setBikeType2(eventRequestDTO.getBikeType2());
            evt.setStartPoint(eventRequestDTO.getStartPoint());
            evt.setEndPoint(eventRequestDTO.getEndPoint());

            eventRepository.save(evt);
            return evt; }

        
        public void deleteEvent(int id) {
            eventRepository.deleteById(id);
        }

        public Event getEvent(int id) {
            Optional<Event> evt = eventRepository.findById(id);
            return evt.get();
        }
}
