package com.example.task.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class SignupDto {
    private String name;
    private String password;
    private String password2;
    private String email;
    private String nickname;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean admin = false;
    private String adminToken = "";
}
