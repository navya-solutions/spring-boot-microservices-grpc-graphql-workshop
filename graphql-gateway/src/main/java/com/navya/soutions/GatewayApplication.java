package com.navya.soutions;

import brave.Tracing;
import brave.grpc.GrpcTracing;
import com.navya.soutions.configuration.AppConfig;
import com.navya.soutions.mapper.AppDetailMapper;
import com.navya.soutions.mapper.PostMapper;
import com.navya.soutions.proxy.GrpcServiceProxyClient;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import zipkin2.Span;
import zipkin2.reporter.Reporter;

@SpringBootApplication
@Slf4j
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public GrpcServiceProxyClient grpcServiceProxyClient(final AppConfig appConfig,
                                                         final AppDetailMapper appDetailMapper,
                                                         final PostMapper postMapper, final GrpcTracing grpcTracing) {
        return new GrpcServiceProxyClient(appConfig.getGrpcServiceHost(),
                Integer.parseInt(appConfig.getGrpcServicePort()),
                appDetailMapper, postMapper, grpcTracing);
    }

    @Bean
    public GrpcTracing grpcCustomTracing(Tracing tracing) {
        final GrpcTracing grpcTracing = GrpcTracing.newBuilder(tracing)
                .grpcPropagationFormatEnabled(true).build();
        return grpcTracing;
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

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }


}
