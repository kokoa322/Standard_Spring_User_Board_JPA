package com.example.task.user.model;


import com.example.task.board.model.BoardEntity;
import com.example.task.board.model.LikesEntity;
import com.example.task.model.TimeEntity;
import com.example.task.user.dto.SignupDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@AllArgsConstructor
@NoArgsConstructor
@Entity // DB 테이블 역할을 합니다.
@Table(name="user")
@Builder
public class UserEntity extends TimeEntity implements UserDetails {

    // ID가 자동으로 생성 및 증가합니다.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;

    @Column(name ="email", nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<BoardEntity> boardList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userEntityId", cascade = CascadeType.ALL)
    private List<LikesEntity> likesList = new ArrayList<>();

    public UserEntity(SignupDto signupDto, UserRoleEnum role) {
        this.name = signupDto.getName();
        this.password = signupDto.getPassword();
        this.email = signupDto.getEmail();
        this.nickname = signupDto.getNickname();
        this.role = role;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

