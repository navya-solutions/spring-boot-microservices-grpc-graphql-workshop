package com.navya.soutions.service;

import com.navya.soutions.common.CustomUtils;
import com.navya.soutions.dto.AppDetail;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AppDetailService {
    public Set<AppDetail> getAppDetails() {
        return Set.of(AppDetail.builder()
                .identifier(CustomUtils.createExternalIdentifier())
                .appName("SAMPLE-GRPC-SERVICE-APP")
                .message("GRPC-SERVICE-MESSAGE")
                .version("1.0")
                .build());
    }


}
