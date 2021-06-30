package com.navya.soutions.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties(prefix = "app-config")
@Getter
@Setter
@Primary
public class AppConfig {
    private String grpcServiceHost, grpcServicePort;
}
