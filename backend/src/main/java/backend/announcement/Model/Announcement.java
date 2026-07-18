package backend.announcement.Model;

import backend.category.Model.BloodCategory;
import backend.common.enums.AnnouncementStatus;
import backend.common.enums.UrgencyLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    // Announcement is for one blood category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private BloodCategory category;

    @Column(name = "quantity_needed", nullable = false)
    private Integer quantityNeeded;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UrgencyLevel urgency;

    @Column(nullable = false, length = 120)
    private String location;

    @Column(length = 300)
    private String description;

    // New announcement starts as ACTIVE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AnnouncementStatus status = AnnouncementStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = AnnouncementStatus.ACTIVE;
        }
    }
}