package com.univ.lille.copainsderoute.platine.dtos.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItineraryPointRequestDTOs {
    
    private int rank;
    
    private Double latitude;

    private Double longitude;

    private int event;
}
