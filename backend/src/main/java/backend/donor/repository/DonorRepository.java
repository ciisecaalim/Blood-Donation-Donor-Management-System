package backend.donor.repository;

import backend.donor.Model.Donor;
import backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonorRepository extends JpaRepository<Donor, Long> {

    boolean existsByUser(User user);

    Optional<Donor> findByUser(User user);
}