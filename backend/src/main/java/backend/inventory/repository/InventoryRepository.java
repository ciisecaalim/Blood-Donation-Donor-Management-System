package backend.inventory.repository;

import backend.inventory.Model.Inventory;
import backend.category.Model.BloodCategory;
import backend.report.response.BloodGroupReportResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {


    Optional<Inventory> findByCategory(BloodCategory category);


    boolean existsByCategory(BloodCategory category);


    @Query("SELECT COALESCE(SUM(i.quantity),0) FROM Inventory i")
    Long sumBloodUnits();


    @Query("""
           SELECT new backend.report.response.BloodGroupReportResponse(
           i.category.bloodGroup,
           SUM(i.quantity)
           )
           FROM Inventory i
           GROUP BY i.category.bloodGroup
           """)
    List<BloodGroupReportResponse> getBloodGroupReport();

}