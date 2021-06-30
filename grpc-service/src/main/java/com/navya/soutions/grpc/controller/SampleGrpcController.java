package com.navya.soutions.grpc.controller;

import com.navya.solutions.grpc.proto.service.AppResourceRequest;
import com.navya.solutions.grpc.proto.service.AppResourceResponse;
import com.navya.solutions.grpc.proto.service.AppResourcesResponse;
import com.navya.soutions.mapper.AppDetailMapper;
import com.navya.soutions.service.AppDetailService;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.util.Set;

@GRpcService
@AllArgsConstructor
@Slf4j(topic = "SAMPLE_GRPC_CONTROLLER")
public class SampleGrpcController extends com.navya.solutions.grpc.proto.service.SampleServiceGrpc.SampleServiceImplBase {

    final private AppDetailService appDetailService;
    final private AppDetailMapper appDetailMapper;

    @Override
    public void getAppDetail(AppResourceRequest request, StreamObserver<AppResourcesResponse> responseObserver) {
        log.info(">>>>>>>>>>>>>>>> GET APP DETAILS REQUEST {}", request);
        final Set<AppResourceResponse> appResourceResponses = appDetailMapper.mapJpaDataModelToGrpcApi(appDetailService.getAppDetails());
        responseObserver.onNext(AppResourcesResponse
                .newBuilder()
                .addAllAppResources(appResourceResponses)
                .build());
        responseObserver.onCompleted();
    }


}
