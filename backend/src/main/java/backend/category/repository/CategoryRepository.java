package backend.category.repository;

import backend.category.Model.BloodCategory;
import backend.common.enums.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository
        extends JpaRepository<BloodCategory, Long> {

    boolean existsByBloodGroupIgnoreCase(String bloodGroup);

    Optional<BloodCategory> findByBloodGroupIgnoreCase(
            String bloodGroup
    );

    List<BloodCategory> findByStatusOrderByIdAsc(
            CategoryStatus status
    );
}