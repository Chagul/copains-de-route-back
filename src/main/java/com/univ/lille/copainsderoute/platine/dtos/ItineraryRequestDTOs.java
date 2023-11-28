package com.univ.lille.copainsderoute.platine.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItineraryRequestDTOs {
    
    private int rank;
    
    private Double latitude;

    private Double longitude;

    private int event;
}
