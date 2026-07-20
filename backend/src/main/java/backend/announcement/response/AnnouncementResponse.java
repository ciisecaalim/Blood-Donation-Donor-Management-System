package backend.announcement.response;

import backend.common.enums.AnnouncementStatus;
import backend.common.enums.UrgencyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementResponse {

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