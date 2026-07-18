package backend.inventory.Model;

import backend.category.entity.BloodCategory;
import backend.common.enums.InventoryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One blood category has one inventory row
    @OneToOne
    @JoinColumn(name = "category_id", nullable = false, unique = true)
    private BloodCategory category;

    @Column(nullable = false)
    private Integer quantity = 0;

    // Status is calculated by service, not selected by user
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InventoryStatus status = InventoryStatus.OUT_OF_STOCK;

    @Column(length = 200)
    private String notes;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        updatedAt = LocalDateTime.now();

        if (quantity == null) {
            quantity = 0;
        }

        if (status == null) {
            status = InventoryStatus.OUT_OF_STOCK;
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}