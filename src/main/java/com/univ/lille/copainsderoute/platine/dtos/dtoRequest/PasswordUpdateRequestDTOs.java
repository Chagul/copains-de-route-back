package com.univ.lille.copainsderoute.platine.dtos.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordUpdateRequestDTOs {
    
    private String oldPassword;
    private String newPassword;
}
