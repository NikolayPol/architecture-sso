package com.example.backend.service;

import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public String getReports() {
        return """
                Reports...
                """;
    }
}
