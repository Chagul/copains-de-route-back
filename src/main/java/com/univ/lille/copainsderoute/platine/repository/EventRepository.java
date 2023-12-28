package com.univ.lille.copainsderoute.platine.repository;

import com.univ.lille.copainsderoute.platine.dtos.dtoRequest.FilterEventRequestDto;
import com.univ.lille.copainsderoute.platine.entity.Event;
import com.univ.lille.copainsderoute.platine.entity.User;

import java.time.LocalDate;

import com.univ.lille.copainsderoute.platine.enums.BikeType;
import com.univ.lille.copainsderoute.platine.enums.RoadType;
import com.univ.lille.copainsderoute.platine.enums.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    // find events of today or after
    public List<Event> findByStartDateGreaterThanEqual(LocalDate date);

    // find events of today or 
    public List<Event> findByStartDate(LocalDate date);

    public List<Event> findByPromoter(User promoter);

    @Query(value = "SELECT e FROM Event e WHERE e.startDate BETWEEN :startDate and :endDate AND e.distance BETWEEN :minDistance AND :maxDistance AND (e.bikeType1 in :bikeTypes or e.bikeType2 in :bikeTypes) AND e.visibility in :visibility AND (e.roadType1 in :roadTypes OR e.roadType2 in :roadTypes OR e.roadType3 in :roadTypes)")
    public List<Event> findByFilter(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("minDistance") int minDistance, @Param("maxDistance") int maxDistance, @Param("visibility")List<Visibility> visibility, @Param("roadTypes")List<RoadType> roadTypes, @Param("bikeTypes")List<BikeType> bikeTypes);
}
