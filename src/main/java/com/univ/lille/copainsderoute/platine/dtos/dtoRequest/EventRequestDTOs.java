package com.univ.lille.copainsderoute.platine.dtos.dtoRequest;

import java.time.LocalDate;
import java.time.LocalTime;

import com.univ.lille.copainsderoute.platine.enums.BikeType;
import com.univ.lille.copainsderoute.platine.enums.RoadType;
import com.univ.lille.copainsderoute.platine.enums.Visibility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTOs {
    
  private int promoter;

  private int maxParticipants;

  private LocalDate startDate;

  private LocalTime startTime;

  private RoadType roadType1;

  private RoadType roadType2;

  private RoadType roadType3;

  private String startPoint;

  private String endPoint;

  private String name;

  private String description;

  private BikeType bikeType1;

  private BikeType bikeType2;
  
  private Visibility visibility;

}
