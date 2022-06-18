package com.example.task.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class BoardDtoReq {
    private Long id;
    private String imgName;
    private String imgPath;
    private String content;
    private String nickname;
    private String email;
    private Long like_ids;
    private Integer layout;
    private Long user_id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


}
