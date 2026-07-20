package backend.report.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * Response-ka ugu weyn ee complete system report.
 *
 * Wuxuu isku keenayaa dhammaan qaybaha system-ka:
 *
 * Users
 * Categories
 * Donors
 * Donations
 * Inventory
 * Announcements
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemReportResponse {

    /*
     * Waqtiga report-ka la soo saaray.
     */
    private LocalDateTime generatedAt;

    /*
     * Summary-ga guud ee system-ka.
     */
    private SystemSummaryResponse summary;

    /*
     * Qaybta users-ka.
     */
    private UserReportSection users;

    /*
     * Qaybta blood categories-ka.
     */
    private CategoryReportSection categories;

    /*
     * Qaybta donors-ka.
     */
    private DonorReportSection donors;

    /*
     * Qaybta donations-ka.
     */
    private DonationReportSection donations;

    /*
     * Qaybta blood inventory-ga.
     */
    private InventoryReportSection inventory;

    /*
     * Qaybta announcements-ka.
     */
    private AnnouncementReportSection announcements;
}