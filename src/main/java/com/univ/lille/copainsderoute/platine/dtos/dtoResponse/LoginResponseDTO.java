package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String login;
    private String token;
}
