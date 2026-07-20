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

    /*
     * Primary key-ga announcement-ka.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Cinwaanka ogeysiiska.
     */
    @Column(nullable = false, length = 100)
    private String title;

    /*
     * Announcement kasta wuxuu la xiriiraa
     * hal blood category.
     *
     * Hal category wuxuu yeelan karaa
     * announcements badan.
     */
    @ManyToOne
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private BloodCategory category;

    /*
     * Tirada blood units-ka loo baahan yahay.
     */
    @Column(
            name = "quantity_needed",
            nullable = false
    )
    private Integer quantityNeeded;

    /*
     * Heerka degdegga:
     * NORMAL, URGENT ama EMERGENCY.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UrgencyLevel urgency;

    /*
     * Goobta dhiigga looga baahan yahay.
     */
    @Column(nullable = false, length = 120)
    private String location;

    /*
     * Faahfaahin dheeraad ah.
     */
    @Column(length = 300)
    private String description;

    /*
     * Announcement cusub wuxuu default ahaan
     * noqonayaa ACTIVE.
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AnnouncementStatus status =
            AnnouncementStatus.ACTIVE;

    /*
     * Waqtiga announcement-ka la sameeyay.
     */
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    /*
     * Method-kan wuxuu shaqaynayaa ka hor
     * inta entity-ga markii ugu horreysay
     * database-ka lagu kaydin.
     */
    @PrePersist
    public void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = AnnouncementStatus.ACTIVE;
        }
    }
}