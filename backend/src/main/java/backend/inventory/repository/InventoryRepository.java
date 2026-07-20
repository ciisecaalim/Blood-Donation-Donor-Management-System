package backend.inventory.repository;

import backend.category.Model.BloodCategory;
import backend.common.enums.InventoryStatus;
import backend.inventory.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    /*
     * Hubi in blood category-ga loo sameeyay
     * inventory record hore.
     */
    boolean existsByCategory(
            BloodCategory category
    );

    /*
     * Soo hel inventory record-ka
     * blood category gaar ah.
     */
    Optional<Inventory> findByCategory(
            BloodCategory category
    );

    /*
     * Tiri inventory records-ka leh status gaar ah.
     *
     * Tusaale:
     * AVAILABLE
     * LOW_STOCK
     * OUT_OF_STOCK
     */
    long countByStatus(
            InventoryStatus status
    );

    /*
     * Isku dar quantity-ga dhammaan
     * inventory records-ka.
     *
     * COALESCE wuxuu soo celinayaa 0
     * haddii inventory table-ku madhan yahay.
     */
    @Query("""
            SELECT COALESCE(SUM(i.quantity), 0)
            FROM Inventory i
            """)
    Long sumBloodUnits();
}