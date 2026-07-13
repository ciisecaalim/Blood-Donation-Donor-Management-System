package backend.category.repository;

import backend.category.entity.BloodCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<BloodCategory, Long> {

    boolean existsByBloodGroup(String bloodGroup);

    Optional<BloodCategory> findByBloodGroup(String bloodGroup);

    List<BloodCategory> findByActiveTrue();
}