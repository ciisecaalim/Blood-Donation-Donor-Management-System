package backend.report.response;

import backend.common.enums.DonorStatus;
import backend.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * Qaybta donors-ka ee complete system report.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorReportSection {

    /*
     * Tirada guud ee donors-ka.
     */
    private long totalDonors;

    /*
     * Tirada ACTIVE donors-ka.
     */
    private long activeDonors;

    /*
     * Tirada donors-ka muddo kooban aan dhiig
     * bixin karin.
     */
    private long temporarilyIneligibleDonors;

    /*
     * Tirada INACTIVE donors-ka.
     */
    private long inactiveDonors;

    /*
     * Tirada male donors-ka.
     */
    private long maleDonors;

    /*
     * Tirada female donors-ka.
     */
    private long femaleDonors;

    /*
     * Liiska dhammaan donors-ka.
     */
    private List<DonorItem> records;

    /*
     * Hal row oo donor report-ka ka mid ah.
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DonorItem {

        private Long id;

        /*
         * Magaca waxaa laga soo qaadanayaa User entity.
         */
        private String fullName;

        /*
         * Email-ka waxaa laga soo qaadanayaa User entity.
         */
        private String email;

        private String phone;

        private String bloodGroup;

        private Integer age;

        private Gender gender;

        private Double weight;

        private String address;

        private DonorStatus status;
    }
}