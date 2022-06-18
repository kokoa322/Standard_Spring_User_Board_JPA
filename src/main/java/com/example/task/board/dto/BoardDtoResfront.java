package com.example.task.board.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDtoResfront {

    private Long board_id;
    private List<List<String>> likes;
    private String imageLink;
    private String content;
    private Integer layout;
    private String email;
    private String nickkname;
    private Long hit;

}
