package com.univ.lille.copainsderoute.platine.dtos.dtoRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.univ.lille.copainsderoute.platine.entity.PointLatLng;
import com.univ.lille.copainsderoute.platine.enums.BikeType;
import com.univ.lille.copainsderoute.platine.enums.RoadType;
import com.univ.lille.copainsderoute.platine.enums.Visibility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class EventRequestDTOs {
    
  private int maxParticipants;

  private LocalDate startDate;

  private LocalTime startTime;

  private RoadType roadType1;

  private RoadType roadType2;

  private RoadType roadType3;

  private String name;

  private String description;

  private BikeType bikeType1;

  private BikeType bikeType2;
  
  private Visibility visibility;

  private int distance;

  private List<PointLatLng> steps;

  private String route;

  private String startAddress;

  private String endAddress;
}
