package backend.report.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * Summary-ga guud ee complete system report.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemSummaryResponse {

    /*
     * Tirada guud ee users-ka.
     */
    private long totalUsers;

    /*
     * Tirada guud ee blood categories-ka.
     */
    private long totalCategories;

    /*
     * Tirada guud ee donors-ka.
     */
    private long totalDonors;

    /*
     * Tirada guud ee donation records-ka.
     */
    private long totalDonations;

    /*
     * Wadarta units-ka dhiigga la donate-gareeyay.
     */
    private long totalDonatedUnits;

    /*
     * Wadarta blood units-ka hadda inventory-ga ku jira.
     */
    private long totalBloodUnits;

    /*
     * Tirada guud ee announcements-ka.
     */
    private long totalAnnouncements;
}