package com.navya.soutions.service;

import com.navya.soutions.dto.AppDetail;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class AppDetailService {
    public Set<AppDetail> getAppDetails() {
        return Set.of(AppDetail.builder()
                .identifier(createIdentifier())
                .appName("SAMPLE-REST-GATEWAY")
                .message("REST-GATEWAY-MESSAGE")
                .version("1.0")
                .build());
    }


    private String createIdentifier() {
        return UUID.randomUUID()
                .toString()
                .toUpperCase(Locale.ROOT)
                .replace("-", "");
    }
}
