package backend.announcement.controller;


import backend.announcement.request.AnnouncementUpdateRequest;
import backend.announcement.request.CreateAnnouncementRequest;
import backend.announcement.response.AnnouncementResponse;
import backend.announcement.service.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {


    private final AnnouncementService announcementService;



    // Constructor Injection
    public AnnouncementController(
            AnnouncementService announcementService
    ) {
        this.announcementService = announcementService;
    }





    // CREATE ANNOUNCEMENT
    // POST /api/announcements
    @PostMapping
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @Valid @RequestBody CreateAnnouncementRequest request
    ) {


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        announcementService.createAnnouncement(request)
                );
    }





    // GET ALL ANNOUNCEMENTS
    // GET /api/announcements
    @GetMapping
    public ResponseEntity<List<AnnouncementResponse>> getAllAnnouncements() {


        return ResponseEntity.ok(
                announcementService.getAllAnnouncements()
        );
    }





// GET ANNOUNCEMENT BY ID
// GET /api/announcements/{
// GET ANNOUNCEMENT BY ID
// GET /api/announcements/{id}
@GetMapping("/{id}")
public ResponseEntity<AnnouncementResponse> getAnnouncementById(
        @PathVariable Long id
) {


    return ResponseEntity.ok(
            announcementService.getAnnouncementById(id)
    );
}





    // GET ACTIVE ANNOUNCEMENTS
    // GET /api/announcements/active
    @GetMapping("/active")
    public ResponseEntity<List<AnnouncementResponse>> getActiveAnnouncements() {


        return ResponseEntity.ok(
                announcementService.getActiveAnnouncements()
        );
    }





    // UPDATE ANNOUNCEMENT
    // PUT /api/announcements/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(
            @PathVariable Long id,
            @Valid @RequestBody AnnouncementUpdateRequest request
    ) {


        return ResponseEntity.ok(
                announcementService.updateAnnouncement(id, request)
        );
    }





    // DELETE ANNOUNCEMENT
    // DELETE /api/announcements/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnnouncement(
            @PathVariable Long id
    ) {


        announcementService.deleteAnnouncement(id);


        return ResponseEntity.ok(
                "Announcement deleted successfully"
        );
    }

}