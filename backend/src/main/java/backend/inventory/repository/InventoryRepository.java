package backend.inventory.repository;

import backend.category.Model.BloodCategory;
import backend.inventory.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByCategory(BloodCategory category);

    boolean existsByCategory(BloodCategory category);
}