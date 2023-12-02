package com.univ.lille.copainsderoute.platine.dtos.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommentRequestDTOs {

    private String content;
    private String userWhoCommented;
    private int event;
    private int likes;
   
}
