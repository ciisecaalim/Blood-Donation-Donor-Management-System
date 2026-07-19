package backend.dashboard.service;


import backend.dashboard.response.DashboardResponse;
import backend.inventory.repository.InventoryRepository;
import backend.donor.repository.DonorRepository;
import org.springframework.stereotype.Service;


@Service
public class DashboardService {


    private final DonorRepository donorRepository;

    private final InventoryRepository inventoryRepository;



    public DashboardService(DonorRepository donorRepository,
                            InventoryRepository inventoryRepository) {

        this.donorRepository = donorRepository;
        this.inventoryRepository = inventoryRepository;

    }



    public DashboardResponse getDashboardData() {


        // Count all donors
        long totalDonors = donorRepository.count();



        // We will connect BloodRequest module later
        long totalBloodRequests = 0;



        // Get total blood quantity from inventory
        Long quantity = inventoryRepository.sumBloodUnits();

        long totalBloodUnits = quantity == null ? 0 : quantity;



        return new DashboardResponse(
                totalDonors,
                totalBloodRequests,
                totalBloodUnits
        );

    }

}