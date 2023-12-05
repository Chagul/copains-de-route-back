package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;

import com.univ.lille.copainsderoute.platine.entity.PointLatLng;

import lombok.Data;

@Data
public class PointResponseDTOs {
    
    private Double latitude;
    private Double longitude;
    private int rank;


    public PointResponseDTOs(PointLatLng point) {
        this.rank = point.getRank();
        this.latitude = point.getLatitude();
        this.longitude = point.getLongitude();
    }
   
    
}
