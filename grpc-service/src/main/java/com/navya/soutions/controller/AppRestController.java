package com.navya.soutions.controller;

import com.navya.soutions.dto.AppDetail;
import com.navya.soutions.service.AppDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@AllArgsConstructor
@Slf4j(topic = "REST_CONTROLLER")
public class AppRestController {

    final private AppDetailService appDetailService;

    @GetMapping("/app")
    public Set<AppDetail> getAppDetails() {
        return appDetailService.getAppDetails();
    }
}
