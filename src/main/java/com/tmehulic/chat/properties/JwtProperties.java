package com.tmehulic.chat.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String secret = "q/sk/CxcwoiVkAmQgBYQlkeMFB5rKgeMdk02oB0XAFg=";
    private long expiration = 86400;
}
