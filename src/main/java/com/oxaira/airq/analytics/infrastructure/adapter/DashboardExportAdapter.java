package com.oxaira.airq.analytics.infrastructure.adapter;

import org.springframework.stereotype.Component;

@Component
public class DashboardExportAdapter {

    public String exportPdf() {

        return "dashboard-report.pdf";

    }
}