package backend.donor.service;


import backend.category.Model.BloodCategory;
import backend.category.repository.CategoryRepository;
import backend.common.enums.DonorStatus;
import backend.donor.Model.Donor;
import backend.donor.repository.DonorRepository;
import backend.donor.request.CreateDonorRequest;
import backend.donor.response.DonorResponse;
import backend.user.Model.User;

import backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import backend.donor.request.DonorUpdateRequest;
import backend.exception.DuplicateResourceException;
import backend.exception.ResourceNotFoundException;

import java.util.List;


@Service
public class DonorService {


    private final DonorRepository donorRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;



    // Constructor Injection
    public DonorService(
            DonorRepository donorRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository
    ) {
        this.donorRepository = donorRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }




    // CREATE DONOR
    @Transactional
    public DonorResponse createDonor(CreateDonorRequest request) {


        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );


        if (donorRepository.existsByUser(user)) {
            throw new DuplicateResourceException(
                    "User already has donor profile"
            );
        }



        BloodCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Blood category not found")
                );



        Donor donor = Donor.builder()
                .user(user)
                .category(category)
                .phone(request.getPhone())
                .address(request.getAddress())
                .age(request.getAge())
                .gender(request.getGender())
                .weight(request.getWeight())
                .status(DonorStatus.ACTIVE)
                .build();



        Donor savedDonor = donorRepository.save(donor);


        return mapToResponse(savedDonor);
    }





    // GET ALL DONORS
    public List<DonorResponse> getAllDonors() {


        return donorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }





    // GET DONOR BY ID
    public DonorResponse getDonorById(Long id) {


        Donor donor = donorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found")
                );


        return mapToResponse(donor);
    }

    // UPDATE DONOR
    @Transactional
    public DonorResponse updateDonor(
            Long id,
            DonorUpdateRequest request
    ) {


        Donor donor = donorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found")
                );


        donor.setPhone(request.getPhone());

        donor.setAddress(request.getAddress());

        donor.setAge(request.getAge());

        donor.setGender(request.getGender());

        donor.setWeight(request.getWeight());


        Donor updatedDonor = donorRepository.save(donor);


        return mapToResponse(updatedDonor);
    }




    // DELETE DONOR
    @Transactional
    public void deleteDonor(Long id) {


        Donor donor = donorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found")
                );


        donorRepository.delete(donor);

    }





    // ENTITY TO RESPONSE
    private DonorResponse mapToResponse(Donor donor) {


        return new DonorResponse(

                donor.getId(),

                donor.getUser()
                        .getFullName(),

                donor.getCategory()
                        .getBloodGroup(),

                donor.getPhone(),

                donor.getAddress(),

                donor.getAge(),

                donor.getGender(),

                donor.getWeight(),

                donor.getStatus(),

                donor.getCreatedAt()
        );
    }

}