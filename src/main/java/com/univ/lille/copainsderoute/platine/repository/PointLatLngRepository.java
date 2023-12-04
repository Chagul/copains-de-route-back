package com.univ.lille.copainsderoute.platine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.PointLatLng;

@Repository
public interface PointLatLngRepository extends JpaRepository<PointLatLng, Integer> {

    List<PointLatLng> findByEvent(Event event);



}