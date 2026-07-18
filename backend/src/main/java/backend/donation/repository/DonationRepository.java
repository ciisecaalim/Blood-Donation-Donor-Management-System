package backend.donation.repository;

import backend.common.enums.DonationStatus;
import backend.donation.Model.Donation;
import backend.donor.Model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByDonor(Donor donor);

    List<Donation> findByDonorId(Long donorId);

    List<Donation> findByStatus(DonationStatus status);

    List<Donation> findByDonationDateBetween(LocalDate fromDate, LocalDate toDate);
}