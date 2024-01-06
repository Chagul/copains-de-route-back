package com.univ.lille.copainsderoute.platine.dtos.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class UserTokenRequestDTOs {

    private String token;

    private String email;
}
