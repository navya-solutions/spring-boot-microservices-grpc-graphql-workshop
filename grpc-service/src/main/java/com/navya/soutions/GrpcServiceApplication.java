package com.navya.soutions;

import brave.Tracing;
import brave.grpc.GrpcTracing;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import zipkin2.Span;
import zipkin2.reporter.Reporter;

import java.util.List;

@SpringBootApplication
@Slf4j
public class GrpcServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner ApplicationRunner(ProjectTestDataLoader projectTestDataLoader) {
        return args -> {
            // setup test data
            projectTestDataLoader.loadTestData();
        };
    }

    @Bean
    public GrpcTracing grpcCustomTracing(Tracing tracing) {
        final List<String> keys = tracing.propagation().keys();
        log.info(">>>>>>>>>> keys {}", keys);
        final GrpcTracing grpcTracing = GrpcTracing.newBuilder(tracing)
                .grpcPropagationFormatEnabled(true).build();
        return grpcTracing;
    }

    //grpc-spring-boot-starter provides @GrpcGlobalInterceptor to allow server-side interceptors to be registered with all
    //server stubs, we are just taking advantage of that to install the server-side gRPC tracer.
    @GRpcGlobalInterceptor
    public ServerInterceptor grpcServerSleuthInterceptor(GrpcTracing grpcTracing) {
        return grpcTracing.newServerInterceptor();
    }


    // Use this for debugging (or if there is no Zipkin server running on port 9411)
    @Bean
    @ConditionalOnProperty(value = "custom.zipkin.enabled", havingValue = "false")
    public Reporter<Span> spanReporter() {
        return new Reporter<Span>() {
            @Override
            public void report(Span span) {
                log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                log.info(span.toString());
            }
        };
    }


}
