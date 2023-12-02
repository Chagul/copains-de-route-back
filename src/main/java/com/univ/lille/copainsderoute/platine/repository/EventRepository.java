package com.univ.lille.copainsderoute.platine.repository;

import com.univ.lille.copainsderoute.platine.entity.Event;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    // find events of today or after
    public List<Event> findByStartDateGreaterThanEqual(LocalDate date);

    // find events of today or 
    public List<Event> findByStartDate(LocalDate date);

}
