package backend.announcement.repository;

import backend.announcement.Model.Announcement;
import backend.common.enums.AnnouncementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository
        extends JpaRepository<Announcement, Long> {

    /*
     * Soo qaad dhammaan announcements-ka
     * leh status-ka la soo gudbiyay.
     */
    List<Announcement> findByStatus(
            AnnouncementStatus status
    );

    /*
     * Tiri announcements-ka leh status gaar ah.
     *
     * Dashboard-ka waxaa loogu isticmaalayaa:
     * AnnouncementStatus.ACTIVE
     */
    long countByStatus(
            AnnouncementStatus status
    );
}