package com.tmehulic.chat.service.user;

import com.tmehulic.chat.model.LoginRequest;
import com.tmehulic.chat.model.RegisterRequest;
import com.tmehulic.chat.model.TokenResponse;
import com.tmehulic.chat.repository.UserRepository;
import com.tmehulic.chat.repository.entity.UserEntity;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<TokenResponse> login(LoginRequest request) {
        return userRepository
                .findByUsername(request.username())
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid credentials")))
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid credentials")))
                .map(user -> new TokenResponse("Generated token"));
    }

    @Override
    public Mono<Void> register(RegisterRequest request) {
        return userRepository
                .existsByUsername(request.username())
                .flatMap(
                        exists ->
                                exists
                                        ? Mono.error(
                                                new ResponseStatusException(
                                                        HttpStatus.CONFLICT, "User already exists"))
                                        : saveUser(request))
                .then();
    }

    private Mono<UserEntity> saveUser(RegisterRequest request) {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        return userRepository.save(user);
    }
}
