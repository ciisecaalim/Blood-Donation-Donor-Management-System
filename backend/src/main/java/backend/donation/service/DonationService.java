package backend.donation.service;


import backend.category.Model.BloodCategory;
import backend.category.repository.CategoryRepository;
import backend.donation.Model.Donation;
import backend.donation.repository.DonationRepository;
import backend.donation.request.DonationRequest;
import backend.donation.request.DonationUpdateRequest;
import backend.donation.response.DonationResponse;
import backend.donor.Model.Donor;
import backend.donor.repository.DonorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import backend.exception.ResourceNotFoundException;

import java.util.List;


@Service
public class DonationService {


    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final CategoryRepository categoryRepository;



    // Constructor Injection
    public DonationService(
            DonationRepository donationRepository,
            DonorRepository donorRepository,
            CategoryRepository categoryRepository
    ) {
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
        this.categoryRepository = categoryRepository;
    }




    // CREATE DONATION
    @Transactional
    public DonationResponse createDonation(DonationRequest request) {


        Donor donor = donorRepository.findById(request.getDonorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found")
                );


        BloodCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Blood category not found")
                );


        Donation donation = Donation.builder()
                .donor(donor)
                .category(category)
                .quantity(request.getQuantity())
                .donationDate(request.getDonationDate())
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();


        Donation savedDonation = donationRepository.save(donation);


        return mapToResponse(savedDonation);
    }





    // GET ALL DONATIONS
    public List<DonationResponse> getAllDonations() {

        return donationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }





    // GET DONATION BY ID
    public DonationResponse getDonationById(Long id) {


        Donation donation = donationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donation not found")
                );


        return mapToResponse(donation);
    }





    // UPDATE DONATION
    @Transactional
    public DonationResponse updateDonation(
            Long id,
            DonationUpdateRequest request
    ) {


        Donation donation = donationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donation not found")
                );


        donation.setQuantity(request.getQuantity());
        donation.setLocation(request.getLocation());
        donation.setNotes(request.getNotes());


        Donation updatedDonation =
                donationRepository.save(donation);


        return mapToResponse(updatedDonation);
    }





    // DELETE DONATION
    @Transactional
    public void deleteDonation(Long id) {


        Donation donation = donationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donation not found")
                );


        donationRepository.delete(donation);
    }





    // CONVERT ENTITY TO RESPONSE
    private DonationResponse mapToResponse(Donation donation) {


        return new DonationResponse(

                donation.getId(),

                donation.getDonor()
                        .getUser()
                        .getFullName(),

                donation.getCategory()
                        .getBloodGroup(),

                donation.getQuantity(),

                donation.getDonationDate(),

                donation.getLocation(),

                donation.getStatus(),

                donation.getNotes()
        );
    }

}