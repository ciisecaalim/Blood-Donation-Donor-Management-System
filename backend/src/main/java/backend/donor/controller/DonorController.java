package backend.donor.controller;


import backend.donor.request.CreateDonorRequest;
import backend.donor.request.DonorUpdateRequest;
import backend.donor.response.DonorResponse;
import backend.donor.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/donors")
public class DonorController {


    private final DonorService donorService;


    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }



    // CREATE DONOR
    @PostMapping
    public ResponseEntity<DonorResponse> createDonor(
            @Valid @RequestBody CreateDonorRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(donorService.createDonor(request));
    }





    // GET ALL DONORS
    @GetMapping
    public ResponseEntity<List<DonorResponse>> getAllDonors() {

        return ResponseEntity.ok(
                donorService.getAllDonors()
        );
    }





    // GET DONOR BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DonorResponse> getDonorById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                donorService.getDonorById(id)
        );
    }





    // UPDATE DONOR
    @PutMapping("/{id}")
    public ResponseEntity<DonorResponse> updateDonor(
            @PathVariable Long id,
            @Valid @RequestBody DonorUpdateRequest request
    ) {

        return ResponseEntity.ok(
                donorService.updateDonor(id, request)
        );
    }





    // DELETE DONOR
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDonor(
            @PathVariable Long id
    ) {

        donorService.deleteDonor(id);

        return ResponseEntity.ok(
                "Donor deleted successfully"
        );
    }

}