package backend.announcement.controller;

import backend.announcement.request.AnnouncementStatusRequest;
import backend.announcement.request.AnnouncementUpdateRequest;
import backend.announcement.request.CreateAnnouncementRequest;
import backend.announcement.response.AnnouncementResponse;
import backend.announcement.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    /*
     * Service-ka ayaa qabta business logic-ka
     * iyo database operations-ka.
     */
    private final AnnouncementService announcementService;

    /*
     * ADMIN ONLY:
     *
     * Samee announcement cusub.
     *
     * POST /api/announcements
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @Valid
            @RequestBody CreateAnnouncementRequest request
    ) {

        AnnouncementResponse response =
                announcementService.createAnnouncement(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /*
     * ADMIN ONLY:
     *
     * Soo qaad dhammaan announcements-ka.
     *
     * Waxaa ku jiri kara:
     * ACTIVE
     * FULFILLED
     * CANCELLED
     *
     * GET /api/announcements
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AnnouncementResponse>>
    getAllAnnouncements() {

        List<AnnouncementResponse> announcements =
                announcementService.getAllAnnouncements();

        return ResponseEntity.ok(announcements);
    }

    /*
     * LOGIN REQUIRED:
     *
     * Soo qaad announcements-ka ACTIVE ah oo keliya.
     *
     * User-ku waa inuu haystaa JWT token sax ah.
     *
     * GET /api/announcements/active
     */
    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AnnouncementResponse>>
    getActiveAnnouncements() {

        List<AnnouncementResponse> announcements =
                announcementService.getActiveAnnouncements();

        return ResponseEntity.ok(announcements);
    }

    /*
     * LOGIN REQUIRED:
     *
     * Soo qaad hal announcement adigoo adeegsanaya ID.
     *
     * GET /api/announcements/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AnnouncementResponse>
    getAnnouncementById(
            @PathVariable Long id
    ) {

        AnnouncementResponse response =
                announcementService.getAnnouncementById(id);

        return ResponseEntity.ok(response);
    }

    /*
     * ADMIN ONLY:
     *
     * Wax ka beddel xogta announcement-ka:
     *
     * title
     * quantityNeeded
     * urgency
     * location
     * description
     *
     * Status-ka endpoint-kan laguma beddelayo.
     *
     * PUT /api/announcements/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnnouncementResponse>
    updateAnnouncement(
            @PathVariable Long id,
            @Valid
            @RequestBody AnnouncementUpdateRequest request
    ) {

        AnnouncementResponse response =
                announcementService.updateAnnouncement(
                        id,
                        request
                );

        return ResponseEntity.ok(response);
    }

    /*
     * ADMIN ONLY:
     *
     * Beddel status-ka announcement-ka.
     *
     * Status-yada la heli karo:
     *
     * ACTIVE
     * FULFILLED
     * CANCELLED
     *
     * PATCH /api/announcements/{id}/status
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnnouncementResponse>
    updateAnnouncementStatus(
            @PathVariable Long id,
            @Valid
            @RequestBody AnnouncementStatusRequest request
    ) {

        AnnouncementResponse response =
                announcementService.updateAnnouncementStatus(
                        id,
                        request
                );

        return ResponseEntity.ok(response);
    }

    /*
     * ADMIN ONLY:
     *
     * Tirtir announcement-ka.
     *
     * DELETE /api/announcements/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAnnouncement(
            @PathVariable Long id
    ) {

        announcementService.deleteAnnouncement(id);

        /*
         * 204 No Content:
         * Tirtiristu way guulaysatay,
         * laakiin response body lama soo celinayo.
         */
        return ResponseEntity.noContent().build();
    }
}