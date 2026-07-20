package backend.announcement.request;

import backend.common.enums.AnnouncementStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnnouncementStatusRequest {

    /*
     * Status-ka cusub ee announcement-ka.
     *
     * Qiimayaasha la oggol yahay:
     * ACTIVE
     * FULFILLED
     * CANCELLED
     */
    @NotNull(message = "Announcement status is required")
    private AnnouncementStatus status;
}