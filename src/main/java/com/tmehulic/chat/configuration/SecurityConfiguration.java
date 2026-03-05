package com.tmehulic.chat.configuration;

import com.tmehulic.chat.properties.JwtProperties;
import com.tmehulic.chat.security.JWTAuthenticationFilter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(
            ServerHttpSecurity http, JWTAuthenticationFilter jwtAuthenticationFilter) {
        http.cors(
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
                                        .pathMatchers(
                                                "/scalar/**",
                                                "/v3/api-docs/**",
                                                "/webjars/**",
                                                "/swagger-ui/**")
                                        .permitAll()
                                        .anyExchange()
                                        .authenticated())
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        return http.build();
    }
}
