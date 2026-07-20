package backend.dashboard.service;

import backend.announcement.repository.AnnouncementRepository;
import backend.common.enums.AnnouncementStatus;
import backend.common.enums.InventoryStatus;
import backend.dashboard.response.DashboardResponse;
import backend.donation.repository.DonationRepository;
import backend.donor.repository.DonorRepository;
import backend.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final DonorRepository donorRepository;

    private final DonationRepository donationRepository;

    private final InventoryRepository inventoryRepository;

    private final AnnouncementRepository announcementRepository;

    public DashboardResponse getDashboardData() {

        long totalDonors =
                donorRepository.count();

        long totalDonations =
                donationRepository.count();

        Long bloodUnits =
                inventoryRepository.sumBloodUnits();

        long totalBloodUnits =
                bloodUnits == null ? 0 : bloodUnits;

        long totalAnnouncements =
                announcementRepository.count();

        long activeAnnouncements =
                announcementRepository.countByStatus(
                        AnnouncementStatus.ACTIVE
                );

        long availableGroups =
                inventoryRepository.countByStatus(
                        InventoryStatus.AVAILABLE
                );

        long lowStockGroups =
                inventoryRepository.countByStatus(
                        InventoryStatus.LOW_STOCK
                );

        long outOfStockGroups =
                inventoryRepository.countByStatus(
                        InventoryStatus.OUT_OF_STOCK
                );

        return DashboardResponse.builder()
                .totalDonors(totalDonors)
                .totalDonations(totalDonations)
                .totalBloodUnits(totalBloodUnits)
                .totalAnnouncements(totalAnnouncements)
                .activeAnnouncements(activeAnnouncements)
                .availableGroups(availableGroups)
                .lowStockGroups(lowStockGroups)
                .outOfStockGroups(outOfStockGroups)
                .build();
    }
}