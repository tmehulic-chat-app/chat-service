package com.tmehulic.chat.controller;

import com.tmehulic.chat.model.LoginRequest;
import com.tmehulic.chat.model.RegisterRequest;
import com.tmehulic.chat.model.TokenResponse;
import com.tmehulic.chat.service.user.UserService;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public Mono<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/register")
    public Mono<Void> register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }
}
