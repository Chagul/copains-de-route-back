package com.univ.lille.copainsderoute.platine.repository;

import com.univ.lille.copainsderoute.platine.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Integer> {
}
