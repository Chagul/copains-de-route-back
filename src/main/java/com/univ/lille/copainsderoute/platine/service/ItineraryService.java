package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.repository.EventRepository;
import com.univ.lille.copainsderoute.platine.repository.ItineraryRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import com.univ.lille.copainsderoute.platine.dtos.ItineraryRequestDTOs;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.Itinerary;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.Comparator;

@Service
@AllArgsConstructor
public class ItineraryService {

    private ItineraryRepository itineraryRepository;
    private EventRepository eventRepository;

    public List<Itinerary> getItinerary() throws RuntimeException {
        return itineraryRepository.findAll();
    }

    public Itinerary createItinerary(ItineraryRequestDTOs itineraryRequestDTOs) throws RuntimeException {
        int event_id = itineraryRequestDTOs.getEvent();
        Optional<Event> event = eventRepository.findById(event_id);

        if (!event.isPresent()) {
            throw new RuntimeException("Event not found");
        }
        Itinerary itinerary = new Itinerary();
        itinerary.setEvent(event.get());
        itinerary.setLatitude(itineraryRequestDTOs.getLatitude());
        itinerary.setLongitude(itineraryRequestDTOs.getLongitude());
        itinerary.setRank(itineraryRequestDTOs.getRank());

        return itineraryRepository.save(itinerary);
    }


    public List<Itinerary> getItineraryByEvent(int id) throws RuntimeException {

        Optional<Event> event = eventRepository.findById(id);
        if (!event.isPresent()) {
            throw new RuntimeException("Event not found");
        }
        List<Itinerary> itineraries = itineraryRepository.findByEvent(event.get());
        if (itineraries.isEmpty()) {
            throw new RuntimeException("Itinerary not found");
        }

        // Utiliser Comparator.comparingInt() pour trier par le champ rank et avoir la liste trier par ordre croissant
       Collections.sort(itineraries, Comparator.comparingInt(Itinerary::getRank));
    
        return itineraries;
    }
    
    public Itinerary patchItinerary(ItineraryRequestDTOs itineraryRequestDTOs, int id) throws RuntimeException {

        Optional<Itinerary> itinerary = itineraryRepository.findById(id);
        if (!itinerary.isPresent()) {
            throw new RuntimeException("Itinerary not found");
        }

        if (itineraryRequestDTOs.getLatitude() != null) {
            itinerary.get().setLatitude(itineraryRequestDTOs.getLatitude());
        }
        if (itineraryRequestDTOs.getLongitude() != null) {
            itinerary.get().setLongitude(itineraryRequestDTOs.getLongitude());
        }
        if (itineraryRequestDTOs.getRank() != 0) {
            itinerary.get().setRank(itineraryRequestDTOs.getRank());
        
        }
        return itineraryRepository.save(itinerary.get());
    }

    public void deleteItinerary(int id) throws RuntimeException {

        Optional<Itinerary> itinerary = itineraryRepository.findById(id);
        if (!itinerary.isPresent()) {
            throw new RuntimeException("Itinerary not found");
        }
        itineraryRepository.delete(itinerary.get());
    }
}
