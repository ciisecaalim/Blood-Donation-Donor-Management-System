package backend.announcement.repository;

import backend.announcement.entity.Announcement;
import backend.common.enums.AnnouncementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findByStatus(AnnouncementStatus status);
}