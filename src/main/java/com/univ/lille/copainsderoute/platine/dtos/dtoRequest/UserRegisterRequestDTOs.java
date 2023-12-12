package com.univ.lille.copainsderoute.platine.dtos.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDTOs {

    private String login;

    private String email;

    private String password;
}
