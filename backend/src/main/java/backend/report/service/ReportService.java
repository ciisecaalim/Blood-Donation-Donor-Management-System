package backend.report.service;


import backend.donor.repository.DonorRepository;
import backend.donation.repository.DonationRepository;
import backend.inventory.repository.InventoryRepository;
import backend.report.response.BloodGroupReportResponse;
import backend.report.response.ReportResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReportService {


    private final DonorRepository donorRepository;

    private final DonationRepository donationRepository;

    private final InventoryRepository inventoryRepository;


    public ReportService(DonorRepository donorRepository,
                         DonationRepository donationRepository,
                         InventoryRepository inventoryRepository) {

        this.donorRepository = donorRepository;
        this.donationRepository = donationRepository;
        this.inventoryRepository = inventoryRepository;

    }



    // Summary Report
    public ReportResponse getReport() {


        long totalDonors = donorRepository.count();


        long totalDonations = donationRepository.count();


        long totalDonatedBlood = donationRepository.sumDonatedBlood();


        long totalBloodAvailable = inventoryRepository.sumBloodUnits();



        return new ReportResponse(
                totalDonors,
                totalDonations,
                totalDonatedBlood,
                totalBloodAvailable
        );
    }




    // Blood Group Report
    public List<BloodGroupReportResponse> getBloodGroupReport() {


        return inventoryRepository.getBloodGroupReport();

    }

}