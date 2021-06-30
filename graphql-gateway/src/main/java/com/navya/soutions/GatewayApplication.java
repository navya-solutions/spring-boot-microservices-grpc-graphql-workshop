package com.navya.soutions;

import com.navya.soutions.configuration.AppConfig;
import com.navya.soutions.mapper.AppDetailMapper;
import com.navya.soutions.proxy.GrpcServiceProxyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public GrpcServiceProxyClient grpcServiceProxyClient(final AppConfig appConfig, final AppDetailMapper appDetailMapper) {
        return new GrpcServiceProxyClient(appConfig.getGrpcServiceHost(),
                Integer.parseInt(appConfig.getGrpcServicePort()),
                appDetailMapper);
    }

}
