package com.univ.lille.copainsderoute.platine.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItineraryRequestDTOs {
    
    private int rank;
    
    private double latitude;

    private double longitude;

    private int event;
}
