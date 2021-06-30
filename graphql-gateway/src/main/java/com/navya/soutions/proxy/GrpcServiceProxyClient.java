package com.navya.soutions.proxy;

import com.navya.solutions.grpc.proto.service.AppResourceRequest;
import com.navya.solutions.grpc.proto.service.AppResourcesResponse;
import com.navya.solutions.grpc.proto.service.SampleServiceGrpc;
import com.navya.soutions.graphql.type.AppDetail;
import com.navya.soutions.mapper.AppDetailMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "GRPC_SERVICE_PROXY_CLIENT")
public class GrpcServiceProxyClient implements GrpcServiceProxy {
    private final ManagedChannel channel;
    private final SampleServiceGrpc.SampleServiceBlockingStub sampleServiceBlockingStub;
    private final AppDetailMapper appDetailMapper;

    public GrpcServiceProxyClient(final String host, int port, final AppDetailMapper appDetailMapper) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        this.sampleServiceBlockingStub = SampleServiceGrpc.newBlockingStub(channel);
        this.appDetailMapper = appDetailMapper;

    }

    @Override
    public Set<AppDetail> getAppDetails() {
        final String identifier = createIdentifier();
        log.info(">>>>>>>>>>>>>>>> GET APP DETAILS REQUEST for {}", identifier);
        final AppResourcesResponse appDetail = sampleServiceBlockingStub.getAppDetail(AppResourceRequest
                .newBuilder()
                .setIdentifier(identifier)
                .build());
        return appDetailMapper.mapJpaDataModelToGrpcApi(appDetail.getAppResourcesList());

    }

    private String createIdentifier() {
        return UUID.randomUUID()
                .toString()
                .toUpperCase(Locale.ROOT)
                .replace("-", "");
    }
}
