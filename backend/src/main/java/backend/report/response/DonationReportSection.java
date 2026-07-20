package backend.report.response;

import backend.common.enums.DonationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * Qaybta donations-ka ee complete system report.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationReportSection {

    /*
     * Tirada guud ee donation records-ka.
     */
    private long totalDonations;

    /*
     * Wadarta blood units-ka la donate-gareeyay.
     */
    private long totalDonatedUnits;

    /*
     * Tirada PENDING donations-ka.
     */
    private long pendingDonations;

    /*
     * Tirada APPROVED donations-ka.
     */
    private long approvedDonations;

    /*
     * Tirada REJECTED donations-ka.
     */
    private long rejectedDonations;

    /*
     * Tirada RECORDED donations-ka.
     */
    private long recordedDonations;

    /*
     * Tirada CANCELLED donations-ka.
     */
    private long cancelledDonations;

    /*
     * Liiska dhammaan donation records-ka.
     */
    private List<DonationItem> records;

    /*
     * Hal row oo donation report-ka ka mid ah.
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DonationItem {

        private Long id;

        private String donorName;

        private String donorEmail;

        private String bloodGroup;

        private Integer quantity;

        private DonationStatus status;
    }
}