package com.oxaira.airq.analytics.interfaces.rest;

import com.oxaira.airq.analytics.application.dto.DashboardResponse;
import com.oxaira.airq.analytics.application.service.GenerateDashboardUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AnalyticsController {

    private final GenerateDashboardUseCase useCase;

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {

        return useCase.execute();

    }
}