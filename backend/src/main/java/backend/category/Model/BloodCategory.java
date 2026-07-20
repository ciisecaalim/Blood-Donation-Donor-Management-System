package backend.category.Model;

import backend.common.enums.CategoryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodCategory {

    /*
     * Primary key-ga blood category-ga.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Blood group-ka:
     *
     * A+
     * A-
     * B+
     * B-
     * AB+
     * AB-
     * O+
     * O-
     */
    @Column(
            name = "blood_group",
            nullable = false,
            unique = true,
            length = 5
    )
    private String bloodGroup;

    /*
     * Faahfaahinta blood category-ga.
     */
    @Column(length = 255)
    private String description;

    /*
     * Status-ka category-ga.
     *
     * Category cusub wuxuu default ahaan
     * noqonayaa ACTIVE.
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoryStatus status =
            CategoryStatus.ACTIVE;

    /*
     * Waqtiga category-ga la sameeyay.
     */
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    /*
     * Waxaa la ordiyaa ka hor inta category-ga
     * database-ka markii ugu horreysay lagu kaydin.
     */
    @PrePersist
    public void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = CategoryStatus.ACTIVE;
        }
    }
}