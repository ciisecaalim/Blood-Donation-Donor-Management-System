package backend.report.controller;

import backend.report.response.SystemReportResponse;
import backend.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controller-ka complete system report.
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    /*
     * Service-ka diyaarinaya complete system report-ka.
     */
    private final ReportService reportService;

    /*
     * ADMIN ONLY:
     *
     * Soo saar complete system report.
     *
     * GET /api/reports/system
     */
    @GetMapping("/system")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemReportResponse>
    getCompleteSystemReport() {

        SystemReportResponse report =
                reportService.getCompleteSystemReport();

        return ResponseEntity.ok(report);
    }
}