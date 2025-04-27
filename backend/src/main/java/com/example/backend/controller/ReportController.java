package com.example.backend.controller;

import com.example.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    @CrossOrigin("*")
    @PreAuthorize("hasRole('prothetic_user')")
    public ResponseEntity<String> getReports() {
        var reports = reportService.getReports();
        return ResponseEntity.ok(reports);
    }
}

