package com.tmehulic.chat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(
                        corsSpec -> {
                            corsSpec.configurationSource(
                                    request -> {
                                        var config = new CorsConfiguration();
                                        config.setAllowedOrigins(List.of("http://localhost:3000"));
                                        config.setAllowedMethods(List.of("*"));
                                        config.setAllowedHeaders(List.of("*"));
                                        return config;
                                    });
                        })
                .authorizeExchange(
                        exchanges ->
                                exchanges
                                        .pathMatchers("/auth/**")
                                        .permitAll()
                                        .anyExchange()
                                        .permitAll());
        return http.build();
    }
}
