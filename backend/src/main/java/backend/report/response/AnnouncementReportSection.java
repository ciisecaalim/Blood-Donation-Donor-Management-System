package backend.report.response;

import backend.common.enums.AnnouncementStatus;
import backend.common.enums.UrgencyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Qaybta announcements-ka ee complete system report.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementReportSection {

    /*
     * Tirada guud ee announcements-ka.
     */
    private long totalAnnouncements;

    /*
     * Tirada ACTIVE announcements-ka.
     */
    private long activeAnnouncements;

    /*
     * Tirada FULFILLED announcements-ka.
     */
    private long fulfilledAnnouncements;

    /*
     * Tirada CANCELLED announcements-ka.
     */
    private long cancelledAnnouncements;

    /*
     * Tirada NORMAL urgency announcements-ka.
     */
    private long normalAnnouncements;

    /*
     * Tirada URGENT announcements-ka.
     */
    private long urgentAnnouncements;

    /*
     * Tirada EMERGENCY announcements-ka.
     */
    private long emergencyAnnouncements;

    /*
     * Liiska dhammaan announcements-ka.
     */
    private List<AnnouncementItem> records;

    /*
     * Hal row oo announcement report-ka ka mid ah.
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnnouncementItem {

        private Long id;

        private String title;

        private String bloodGroup;

        private Integer quantityNeeded;

        private UrgencyLevel urgency;

        private String location;

        private String description;

        private AnnouncementStatus status;

        private LocalDateTime createdAt;
    }
}