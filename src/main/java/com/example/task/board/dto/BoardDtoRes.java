package com.example.task.board.dto;

import com.example.task.board.model.BoardEntity;
import com.example.task.board.model.LikesEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDtoRes {

    private Long board_id;

    private List<List<String>> likes;
    private String imageLink;
    private String content;
    private Integer layout;

    private String email;

    private String nickname;

    private Long hit;


    public BoardDtoRes(BoardEntity boardEntity) {
        this.imageLink = boardEntity.getImgPath();
        this.content = boardEntity.getContent();
        this.layout = boardEntity.getLayout();
        this.board_id = boardEntity.getId();
    }

    public static BoardDtoRes BoardDtoRes(BoardEntity boardEntity) {
        /** 좋아요 한 아이디 객체들을 이메일과 닉네임만 보이게 처리 */
        List<List<String>> likeIds = new ArrayList<>();
        if(boardEntity.getLikesList() != null) {
            for (LikesEntity likesObj : boardEntity.getLikesList()) {
                List<String> tempLikesList = new ArrayList<>();
                tempLikesList.add(likesObj.getUserEntityId().getEmail());
                tempLikesList.add(likesObj.getUserEntityId().getNickname());
                likeIds.add(tempLikesList);
            }
        } else if(boardEntity.getLikesList() == null) {
           return BoardDtoRes.builder()
                    .board_id(boardEntity.getId())
                    .content(boardEntity.getContent())
                    .layout(boardEntity.getLayout())
                    .email(boardEntity.getUserEntity().getEmail())
                    .imageLink(boardEntity.getImgPath())
                    .nickname(boardEntity.getNickname())
                    .likes(likeIds)
                    .hit(Long.valueOf(0))
                    .build();
        }

        return BoardDtoRes.builder()
                .board_id(boardEntity.getId())
                .content(boardEntity.getContent())
                .layout(boardEntity.getLayout())
                .email(boardEntity.getUserEntity().getEmail())
                .imageLink(boardEntity.getImgPath())
                .nickname(boardEntity.getNickname())
                .likes(likeIds)
                .hit(Long.valueOf(boardEntity.getLikesList().size()))
                .build();
    }
}
