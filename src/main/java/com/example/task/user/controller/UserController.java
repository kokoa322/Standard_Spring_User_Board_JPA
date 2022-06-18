package com.example.task.user.controller;



import com.example.task.secuirty.JwtTokenProvider;
import com.example.task.user.dto.SignupDto;
import com.example.task.user.model.UserEntity;
import com.example.task.user.model.UserRoleEnum;
import com.example.task.user.repository.UserRepository;
import com.example.task.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@RestController
@Controller
@RequestMapping("/api")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private final UserService userService;

    // 회원 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody SignupDto userDto) {

        return userService.login(userDto);
    }

    // 회원 가입 페이지
    @GetMapping("/register")
    public String signup() {

        return "register";
    }

    // 회원 가입 요청 처리
    @PostMapping("/register")
    public String registerUser(@RequestBody SignupDto signupDto) {
        userService.registerUser(signupDto);
        return "register";
    }

}
