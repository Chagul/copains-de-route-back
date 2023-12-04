package com.univ.lille.copainsderoute.platine.dtos.dtoRequest;

import com.univ.lille.copainsderoute.platine.entity.Friends;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTOs {


    private String login;

    private String email;

    private String password;

    
}
