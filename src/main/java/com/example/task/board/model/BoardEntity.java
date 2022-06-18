package com.example.task.board.model;


import com.example.task.board.dto.BoardDtoReq;
import com.example.task.model.TimeEntity;
import com.example.task.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name="board")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardEntity extends TimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BOARD_ID")
    private Long id;
    @Column(nullable = true)
    private String imgName;
    @Column(nullable = true)
    private String imgPath;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Integer layout;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity userEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "boardEntityId", cascade = CascadeType.ALL)
    private List<LikesEntity> likesList = new ArrayList<>();

    public BoardEntity(BoardDtoReq boardDtoReq, UserEntity userEntity){
        this.imgName = boardDtoReq.getImgName();
        this.imgPath = boardDtoReq.getImgPath();
        this.content = boardDtoReq.getContent();
        this.nickname = boardDtoReq.getNickname();
        this.layout = boardDtoReq.getLayout();
        this.userEntity = userEntity;

    }

    public void update(BoardDtoReq boardDtoReq, UserEntity userEntity) {
        this.imgName = boardDtoReq.getImgName();
        this.imgPath = boardDtoReq.getImgPath();
        this.content = boardDtoReq.getContent();
        this.nickname = boardDtoReq.getNickname();
        this.layout = boardDtoReq.getLayout();
        this.userEntity = userEntity;
    }
}
