package com.univ.lille.copainsderoute.platine.dtos.dtoRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.univ.lille.copainsderoute.platine.entity.PointLatLng;
import com.univ.lille.copainsderoute.platine.enums.BikeType;
import com.univ.lille.copainsderoute.platine.enums.RoadType;
import com.univ.lille.copainsderoute.platine.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class FilterEventRequestDto {

    private int distanceMin;

    private int distanceMax;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<RoadType> roadTypes;

    private List<BikeType> bikeTypes;

    private List<Visibility> visibility;
}
