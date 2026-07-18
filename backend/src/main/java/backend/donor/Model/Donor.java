package backend.donor.Model;

import backend.category.Model.BloodCategory;
import backend.common.enums.DonorStatus;
import backend.common.enums.Gender;
import backend.user.Model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "donors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One user account can have one donor profile
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Donor belongs to one blood category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private BloodCategory category;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 120)
    private String address;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Column
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private DonorStatus status = DonorStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = DonorStatus.ACTIVE;
        }
    }
}