package backend.report.controller;


import backend.report.response.BloodGroupReportResponse;
import backend.report.response.ReportResponse;
import backend.report.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/report")
public class ReportController {


    private final ReportService reportService;


    public ReportController(ReportService reportService) {

        this.reportService = reportService;

    }



    // Summary report
    @GetMapping
    public ResponseEntity<ReportResponse> getReport() {

        return ResponseEntity.ok(
                reportService.getReport()
        );

    }



    // Blood group report
    @GetMapping("/blood-groups")
    public ResponseEntity<List<BloodGroupReportResponse>> getBloodGroupReport() {

        return ResponseEntity.ok(
                reportService.getBloodGroupReport()
        );

    }

}