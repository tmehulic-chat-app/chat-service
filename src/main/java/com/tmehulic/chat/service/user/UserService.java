package com.tmehulic.chat.service.user;

import com.tmehulic.chat.model.LoginRequest;
import com.tmehulic.chat.model.RegisterRequest;
import com.tmehulic.chat.model.TokenResponse;

import jakarta.validation.Valid;

import reactor.core.publisher.Mono;

public interface UserService {

    Mono<TokenResponse> login(@Valid LoginRequest request);

    Mono<Void> register(@Valid RegisterRequest request);

}
