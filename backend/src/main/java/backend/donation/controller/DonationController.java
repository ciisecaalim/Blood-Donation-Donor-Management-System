package backend.donation.controller;


import backend.donation.request.DonationRequest;
import backend.donation.request.DonationUpdateRequest;
import backend.donation.response.DonationResponse;
import backend.donation.service.DonationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/donations")
public class DonationController {


    private final DonationService donationService;


    // Constructor Injection
    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }



    // CREATE DONATION
    // POST /api/donations
    @PostMapping
    public ResponseEntity<DonationResponse> createDonation(
            @Valid @RequestBody DonationRequest request
    ) {

        DonationResponse response =
                donationService.createDonation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }





    // GET ALL DONATIONS
    // GET /api/donations
    @GetMapping
    public ResponseEntity<List<DonationResponse>> getAllDonations() {

        return ResponseEntity.ok(
                donationService.getAllDonations()
        );
    }





    // GET DONATION BY ID
    // GET /api/donations/{id}
    @GetMapping("/{id}")
    public ResponseEntity<DonationResponse> getDonationById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                donationService.getDonationById(id)
        );
    }





    // UPDATE DONATION
    // PUT /api/donations/{id}
    @PutMapping("/{id}")
    public ResponseEntity<DonationResponse> updateDonation(
            @PathVariable Long id,
            @Valid @RequestBody DonationUpdateRequest request
    ) {

        return ResponseEntity.ok(
                donationService.updateDonation(id, request)
        );
    }





    // DELETE DONATION
    // DELETE /api/donations/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDonation(
            @PathVariable Long id
    ) {

        donationService.deleteDonation(id);

        return ResponseEntity.ok(
                "Donation deleted successfully"
        );
    }

}