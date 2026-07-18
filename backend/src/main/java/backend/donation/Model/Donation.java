package backend.donation.Model;

import backend.category.entity.BloodCategory;
import backend.common.enums.DonationStatus;
import backend.donor.Model.Donor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Donation-ku wuxuu ku xiran yahay donor
    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;

    // Donation-ku wuxuu ku xiran yahay blood category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private BloodCategory category;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "donation_date", nullable = false)
    private LocalDate donationDate;

    @Column(nullable = false, length = 120)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DonationStatus status = DonationStatus.PENDING;

    @Column(length = 250)
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = DonationStatus.PENDING;
        }
    }
}