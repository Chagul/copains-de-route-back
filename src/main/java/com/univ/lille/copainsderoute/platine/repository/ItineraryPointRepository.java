package com.univ.lille.copainsderoute.platine.repository;

import com.univ.lille.copainsderoute.platine.entity.ItineraryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryPointRepository extends JpaRepository<ItineraryPoint, Integer> {

    List<ItineraryPoint> findByEvent(int id);
}
