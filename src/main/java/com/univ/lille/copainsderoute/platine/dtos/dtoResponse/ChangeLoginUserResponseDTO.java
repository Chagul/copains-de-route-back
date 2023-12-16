package com.univ.lille.copainsderoute.platine.dtos.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeLoginUserResponseDTO {
    private UserResponseDTOs user;
    private LoginResponseDTO login;
}
