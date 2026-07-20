package backend.report.service;

import backend.announcement.Model.Announcement;
import backend.announcement.repository.AnnouncementRepository;
import backend.category.Model.BloodCategory;
import backend.category.repository.CategoryRepository;
import backend.common.enums.AnnouncementStatus;
import backend.common.enums.CategoryStatus;
import backend.common.enums.DonationStatus;
import backend.common.enums.DonorStatus;
import backend.common.enums.Gender;
import backend.common.enums.InventoryStatus;
import backend.common.enums.UrgencyLevel;
import backend.common.enums.UserRole;
import backend.common.enums.UserStatus;
import backend.donation.Model.Donation;
import backend.donation.repository.DonationRepository;
import backend.donor.Model.Donor;
import backend.donor.repository.DonorRepository;
import backend.inventory.Model.Inventory;
import backend.inventory.repository.InventoryRepository;
import backend.report.response.AnnouncementReportSection;
import backend.report.response.CategoryReportSection;
import backend.report.response.DonationReportSection;
import backend.report.response.DonorReportSection;
import backend.report.response.InventoryReportSection;
import backend.report.response.SystemReportResponse;
import backend.report.response.SystemSummaryResponse;
import backend.report.response.UserReportSection;
import backend.user.Model.User;
import backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Service-kan wuxuu soo saarayaa hal complete system report.
 *
 * Report-ka wuxuu ka soo ururinayaa xogta:
 *
 * Users
 * Categories
 * Donors
 * Donations
 * Inventory
 * Announcements
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final DonorRepository donorRepository;

    private final DonationRepository donationRepository;

    private final InventoryRepository inventoryRepository;

    private final AnnouncementRepository announcementRepository;

    /*
     * Soo saar complete system report.
     */
    public SystemReportResponse getCompleteSystemReport() {

        /*
         * Soo qaad dhammaan records-ka.
         *
         * Records-ka ugu dambeeyay ayaa marka hore
         * report-ka kasoo muuqanaya.
         */
        List<User> users =
                userRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "id")
                );

        List<BloodCategory> categories =
                categoryRepository.findAll(
                        Sort.by(Sort.Direction.ASC, "id")
                );

        List<Donor> donors =
                donorRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "id")
                );

        List<Donation> donations =
                donationRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "id")
                );

        List<Inventory> inventories =
                inventoryRepository.findAll(
                        Sort.by(Sort.Direction.ASC, "id")
                );

        List<Announcement> announcements =
                announcementRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "id")
                );

        /*
         * Samee qayb kasta oo report-ka ka mid ah.
         */
        UserReportSection userSection =
                buildUserSection(users);

        CategoryReportSection categorySection =
                buildCategorySection(categories);

        DonorReportSection donorSection =
                buildDonorSection(donors);

        DonationReportSection donationSection =
                buildDonationSection(donations);

        InventoryReportSection inventorySection =
                buildInventorySection(inventories);

        AnnouncementReportSection announcementSection =
                buildAnnouncementSection(announcements);

        /*
         * Samee summary-ga guud.
         */
        SystemSummaryResponse summary =
                SystemSummaryResponse.builder()
                        .totalUsers(users.size())
                        .totalCategories(categories.size())
                        .totalDonors(donors.size())
                        .totalDonations(donations.size())
                        .totalDonatedUnits(
                                donationSection.getTotalDonatedUnits()
                        )
                        .totalBloodUnits(
                                inventorySection.getTotalBloodUnits()
                        )
                        .totalAnnouncements(
                                announcements.size()
                        )
                        .build();

        /*
         * Isku dar dhammaan sections-ka
         * hal SystemReportResponse.
         */
        return SystemReportResponse.builder()
                .generatedAt(LocalDateTime.now())
                .summary(summary)
                .users(userSection)
                .categories(categorySection)
                .donors(donorSection)
                .donations(donationSection)
                .inventory(inventorySection)
                .announcements(announcementSection)
                .build();
    }

    /*
     * Samee user report section.
     */
    private UserReportSection buildUserSection(
            List<User> users
    ) {

        long activeUsers =
                users.stream()
                        .filter(user ->
                                user.getStatus()
                                        == UserStatus.ACTIVE
                        )
                        .count();

        long inactiveUsers =
                users.stream()
                        .filter(user ->
                                user.getStatus()
                                        == UserStatus.INACTIVE
                        )
                        .count();

        long blockedUsers =
                users.stream()
                        .filter(user ->
                                user.getStatus()
                                        == UserStatus.BLOCKED
                        )
                        .count();

        long totalAdmins =
                users.stream()
                        .filter(user ->
                                user.getRole()
                                        == UserRole.ROLE_ADMIN
                        )
                        .count();

        long totalStaff =
                users.stream()
                        .filter(user ->
                                user.getRole()
                                        == UserRole.ROLE_STAFF
                        )
                        .count();

        long totalRegularUsers =
                users.stream()
                        .filter(user ->
                                user.getRole()
                                        == UserRole.ROLE_USER
                        )
                        .count();

        List<UserReportSection.UserItem> records =
                users.stream()
                        .map(this::mapUserItem)
                        .toList();

        return UserReportSection.builder()
                .totalUsers(users.size())
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .blockedUsers(blockedUsers)
                .totalAdmins(totalAdmins)
                .totalStaff(totalStaff)
                .totalRegularUsers(totalRegularUsers)
                .records(records)
                .build();
    }

    /*
     * Samee category report section.
     */
    private CategoryReportSection buildCategorySection(
            List<BloodCategory> categories
    ) {

        long activeCategories =
                categories.stream()
                        .filter(category ->
                                category.getStatus()
                                        == CategoryStatus.ACTIVE
                        )
                        .count();

        long inactiveCategories =
                categories.stream()
                        .filter(category ->
                                category.getStatus()
                                        == CategoryStatus.INACTIVE
                        )
                        .count();

        List<CategoryReportSection.CategoryItem> records =
                categories.stream()
                        .map(this::mapCategoryItem)
                        .toList();

        return CategoryReportSection.builder()
                .totalCategories(categories.size())
                .activeCategories(activeCategories)
                .inactiveCategories(inactiveCategories)
                .records(records)
                .build();
    }

    /*
     * Samee donor report section.
     */
    private DonorReportSection buildDonorSection(
            List<Donor> donors
    ) {

        long activeDonors =
                donors.stream()
                        .filter(donor ->
                                donor.getStatus()
                                        == DonorStatus.ACTIVE
                        )
                        .count();

        long temporarilyIneligibleDonors =
                donors.stream()
                        .filter(donor ->
                                donor.getStatus()
                                        == DonorStatus.TEMPORARILY_INELIGIBLE
                        )
                        .count();

        long inactiveDonors =
                donors.stream()
                        .filter(donor ->
                                donor.getStatus()
                                        == DonorStatus.INACTIVE
                        )
                        .count();

        long maleDonors =
                donors.stream()
                        .filter(donor ->
                                donor.getGender()
                                        == Gender.MALE
                        )
                        .count();

        long femaleDonors =
                donors.stream()
                        .filter(donor ->
                                donor.getGender()
                                        == Gender.FEMALE
                        )
                        .count();

        List<DonorReportSection.DonorItem> records =
                donors.stream()
                        .map(this::mapDonorItem)
                        .toList();

        return DonorReportSection.builder()
                .totalDonors(donors.size())
                .activeDonors(activeDonors)
                .temporarilyIneligibleDonors(
                        temporarilyIneligibleDonors
                )
                .inactiveDonors(inactiveDonors)
                .maleDonors(maleDonors)
                .femaleDonors(femaleDonors)
                .records(records)
                .build();
    }

    /*
     * Samee donation report section.
     */
    private DonationReportSection buildDonationSection(
            List<Donation> donations
    ) {

        long pendingDonations =
                donations.stream()
                        .filter(donation ->
                                donation.getStatus()
                                        == DonationStatus.PENDING
                        )
                        .count();

        long approvedDonations =
                donations.stream()
                        .filter(donation ->
                                donation.getStatus()
                                        == DonationStatus.APPROVED
                        )
                        .count();

        long rejectedDonations =
                donations.stream()
                        .filter(donation ->
                                donation.getStatus()
                                        == DonationStatus.REJECTED
                        )
                        .count();

        long recordedDonations =
                donations.stream()
                        .filter(donation ->
                                donation.getStatus()
                                        == DonationStatus.RECORDED
                        )
                        .count();

        long cancelledDonations =
                donations.stream()
                        .filter(donation ->
                                donation.getStatus()
                                        == DonationStatus.CANCELLED
                        )
                        .count();

        /*
         * Isku dar quantity-ga dhammaan donations-ka.
         */
        long totalDonatedUnits =
                donations.stream()
                        .mapToLong(donation ->
                                safeInteger(
                                        donation.getQuantity()
                                )
                        )
                        .sum();

        List<DonationReportSection.DonationItem> records =
                donations.stream()
                        .map(this::mapDonationItem)
                        .toList();

        return DonationReportSection.builder()
                .totalDonations(donations.size())
                .totalDonatedUnits(totalDonatedUnits)
                .pendingDonations(pendingDonations)
                .approvedDonations(approvedDonations)
                .rejectedDonations(rejectedDonations)
                .recordedDonations(recordedDonations)
                .cancelledDonations(cancelledDonations)
                .records(records)
                .build();
    }

    /*
     * Samee inventory report section.
     */
    private InventoryReportSection buildInventorySection(
            List<Inventory> inventories
    ) {

        long availableGroups =
                inventories.stream()
                        .filter(inventory ->
                                inventory.getStatus()
                                        == InventoryStatus.AVAILABLE
                        )
                        .count();

        long lowStockGroups =
                inventories.stream()
                        .filter(inventory ->
                                inventory.getStatus()
                                        == InventoryStatus.LOW_STOCK
                        )
                        .count();

        long outOfStockGroups =
                inventories.stream()
                        .filter(inventory ->
                                inventory.getStatus()
                                        == InventoryStatus.OUT_OF_STOCK
                        )
                        .count();

        /*
         * Isku dar quantity-ga dhammaan inventory records-ka.
         */
        long totalBloodUnits =
                inventories.stream()
                        .mapToLong(inventory ->
                                safeInteger(
                                        inventory.getQuantity()
                                )
                        )
                        .sum();

        List<InventoryReportSection.InventoryItem> records =
                inventories.stream()
                        .map(this::mapInventoryItem)
                        .toList();

        return InventoryReportSection.builder()
                .totalBloodUnits(totalBloodUnits)
                .availableGroups(availableGroups)
                .lowStockGroups(lowStockGroups)
                .outOfStockGroups(outOfStockGroups)
                .records(records)
                .build();
    }

    /*
     * Samee announcement report section.
     */
    private AnnouncementReportSection buildAnnouncementSection(
            List<Announcement> announcements
    ) {

        long activeAnnouncements =
                announcements.stream()
                        .filter(announcement ->
                                announcement.getStatus()
                                        == AnnouncementStatus.ACTIVE
                        )
                        .count();

        long fulfilledAnnouncements =
                announcements.stream()
                        .filter(announcement ->
                                announcement.getStatus()
                                        == AnnouncementStatus.FULFILLED
                        )
                        .count();

        long cancelledAnnouncements =
                announcements.stream()
                        .filter(announcement ->
                                announcement.getStatus()
                                        == AnnouncementStatus.CANCELLED
                        )
                        .count();

        long normalAnnouncements =
                announcements.stream()
                        .filter(announcement ->
                                announcement.getUrgency()
                                        == UrgencyLevel.NORMAL
                        )
                        .count();

        long urgentAnnouncements =
                announcements.stream()
                        .filter(announcement ->
                                announcement.getUrgency()
                                        == UrgencyLevel.URGENT
                        )
                        .count();

        long emergencyAnnouncements =
                announcements.stream()
                        .filter(announcement ->
                                announcement.getUrgency()
                                        == UrgencyLevel.EMERGENCY
                        )
                        .count();

        List<AnnouncementReportSection.AnnouncementItem> records =
                announcements.stream()
                        .map(this::mapAnnouncementItem)
                        .toList();

        return AnnouncementReportSection.builder()
                .totalAnnouncements(announcements.size())
                .activeAnnouncements(activeAnnouncements)
                .fulfilledAnnouncements(fulfilledAnnouncements)
                .cancelledAnnouncements(cancelledAnnouncements)
                .normalAnnouncements(normalAnnouncements)
                .urgentAnnouncements(urgentAnnouncements)
                .emergencyAnnouncements(emergencyAnnouncements)
                .records(records)
                .build();
    }

    /*
     * User entity-ga u beddel report item.
     */
    private UserReportSection.UserItem mapUserItem(
            User user
    ) {

        return UserReportSection.UserItem.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    /*
     * BloodCategory entity-ga u beddel report item.
     */
    private CategoryReportSection.CategoryItem mapCategoryItem(
            BloodCategory category
    ) {

        return CategoryReportSection.CategoryItem.builder()
                .id(category.getId())
                .bloodGroup(category.getBloodGroup())
                .description(category.getDescription())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .build();
    }

    /*
     * Donor entity-ga u beddel report item.
     */
    private DonorReportSection.DonorItem mapDonorItem(
            Donor donor
    ) {

        return DonorReportSection.DonorItem.builder()
                .id(donor.getId())
                .fullName(
                        donor.getUser().getFullName()
                )
                .email(
                        donor.getUser().getEmail()
                )
                .phone(donor.getPhone())
                .bloodGroup(
                        donor.getCategory().getBloodGroup()
                )
                .age(donor.getAge())
                .gender(donor.getGender())
                .weight(donor.getWeight())
                .address(donor.getAddress())
                .status(donor.getStatus())
                .build();
    }

    /*
     * Donation entity-ga u beddel report item.
     */
    private DonationReportSection.DonationItem mapDonationItem(
            Donation donation
    ) {

        return DonationReportSection.DonationItem.builder()
                .id(donation.getId())
                .donorName(
                        donation.getDonor()
                                .getUser()
                                .getFullName()
                )
                .donorEmail(
                        donation.getDonor()
                                .getUser()
                                .getEmail()
                )
                .bloodGroup(
                        donation.getCategory()
                                .getBloodGroup()
                )
                .quantity(donation.getQuantity())
                .status(donation.getStatus())
                .build();
    }

    /*
     * Inventory entity-ga u beddel report item.
     */
    private InventoryReportSection.InventoryItem mapInventoryItem(
            Inventory inventory
    ) {

        return InventoryReportSection.InventoryItem.builder()
                .id(inventory.getId())
                .bloodGroup(
                        inventory.getCategory()
                                .getBloodGroup()
                )
                .quantity(inventory.getQuantity())
                .status(inventory.getStatus())
                .build();
    }

    /*
     * Announcement entity-ga u beddel report item.
     */
    private AnnouncementReportSection.AnnouncementItem
    mapAnnouncementItem(
            Announcement announcement
    ) {

        return AnnouncementReportSection
                .AnnouncementItem
                .builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .bloodGroup(
                        announcement.getCategory()
                                .getBloodGroup()
                )
                .quantityNeeded(
                        announcement.getQuantityNeeded()
                )
                .urgency(announcement.getUrgency())
                .location(announcement.getLocation())
                .description(
                        announcement.getDescription()
                )
                .status(announcement.getStatus())
                .createdAt(
                        announcement.getCreatedAt()
                )
                .build();
    }

    /*
     * Integer null yahay wuxuu u beddelayaa zero.
     *
     * Tani waxay ka hortagaysaa NullPointerException
     * marka quantity la isku darayo.
     */
    private long safeInteger(Integer value) {

        return value == null ? 0L : value.longValue();
    }
}