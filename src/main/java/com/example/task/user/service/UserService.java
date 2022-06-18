package com.example.task.user.service;

import com.example.task.secuirty.JwtTokenProvider;
import com.example.task.user.dto.SignupDto;
import com.example.task.user.model.UserEntity;
import com.example.task.user.model.UserRoleEnum;
import com.example.task.user.repository.UserRepository;
import com.example.task.util.IsValidPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;



    public void registerUser(SignupDto signupDto) {
// 회원 ID 중복 확인
        String nickname = signupDto.getNickname();

        System.out.println(signupDto.getNickname());

        Optional<UserEntity> found = userRepository.findByName(nickname);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 Nickname이 존재합니다.");
        }

        String password = signupDto.getPassword();
        String password2 = signupDto.getPassword2();

        IsValidPassword.isValidPassword(password);

        if (nickname.length() < 3){ //닉네임 3자 이상
            throw new IllegalArgumentException("닉네임 3자이상 작성해주세요");
        } else if (!Pattern.matches("^[a-zA-Z0-9]*$", nickname)) { // 한영 숫자 포함
            throw new IllegalArgumentException("한영 숫자 포함해주세요");
        } else if (password.length() < 4){ //password 4자 이상
            throw new IllegalArgumentException("비밀번호는 4자 이상입니다.");
        } else if (password==password2){ // password 확인
            throw new IllegalArgumentException("비밀먼호가 불일지합니다.");
        }

// 패스워드 암호화

        signupDto.setPassword(passwordEncoder.encode(password));

// 이메일 중복 확인
        String email = signupDto.getEmail();
        Optional<UserEntity> foundEmail = userRepository.findByEmail(email);
        if (foundEmail.isPresent()) {
            System.out.println(foundEmail.get().getEmail());
            throw new IllegalArgumentException("중복된 이메일이 존재합니다");
        }


        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupDto.isAdmin()) {
            if (!signupDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        UserEntity userEntity = new UserEntity(signupDto, role);
        System.out.println(userEntity.getEmail());

        userRepository.save(userEntity);
    }

    public String login(SignupDto userDto) {

        //사용자 이메일
        String email = userDto.getEmail();

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("해당 이메일이 없습니다."));

        //사용자 비밀번호
        String password = userDto.getPassword();

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(userEntity.getEmail(), userEntity.getRoles());

    }
}
