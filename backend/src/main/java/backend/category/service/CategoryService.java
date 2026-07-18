package backend.category.service;

import backend.category.Model.BloodCategory;
import backend.category.repository.CategoryRepository;
import backend.category.request.CategoryRequest;
import backend.category.response.CategoryResponse;
import backend.common.enums.CategoryStatus;
import backend.exception.BadRequestException;
import backend.exception.DuplicateResourceException;
import backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /*
     * Soo qaad dhammaan categories-ka.
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository
                .findAll(
                        Sort.by(
                                Sort.Direction.ASC,
                                "id"
                        )
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /*
     * Soo qaad categories-ka ACTIVE ah oo keliya.
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {

        return categoryRepository
                .findByStatusOrderByIdAsc(
                        CategoryStatus.ACTIVE
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /*
     * Soo qaad hal category.
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(
            Long categoryId
    ) {

        BloodCategory category =
                findCategoryEntityById(categoryId);

        return toResponse(category);
    }

    /*
     * Method-kan waxaa isticmaali kara modules-ka kale,
     * sida Donor, Donation iyo Inventory.
     */
    @Transactional(readOnly = true)
    public BloodCategory findCategoryEntityById(
            Long categoryId
    ) {

        if (categoryId == null) {
            throw new BadRequestException(
                    "Category id is required"
            );
        }

        return categoryRepository
                .findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Blood category not found with id: "
                                        + categoryId
                        )
                );
    }

    /*
     * Samee category cusub.
     */
    @Transactional
    public CategoryResponse createCategory(
            CategoryRequest request
    ) {

        if (request == null) {
            throw new BadRequestException(
                    "Category request is required"
            );
        }

        String bloodGroup =
                normalizeBloodGroup(
                        request.getBloodGroup()
                );

        boolean categoryExists =
                categoryRepository
                        .existsByBloodGroupIgnoreCase(
                                bloodGroup
                        );

        if (categoryExists) {
            throw new DuplicateResourceException(
                    "Blood category already exists: "
                            + bloodGroup
            );
        }

        BloodCategory category =
                BloodCategory.builder()
                        .bloodGroup(bloodGroup)
                        .description(
                                normalizeDescription(
                                        request.getDescription()
                                )
                        )
                        .status(CategoryStatus.ACTIVE)
                        .build();

        BloodCategory savedCategory =
                categoryRepository.save(category);

        return toResponse(savedCategory);
    }

    /*
     * Wax ka beddel category.
     */
    @Transactional
    public CategoryResponse updateCategory(
            Long categoryId,
            CategoryRequest request
    ) {

        if (request == null) {
            throw new BadRequestException(
                    "Category request is required"
            );
        }

        BloodCategory category =
                findCategoryEntityById(categoryId);

        String bloodGroup =
                normalizeBloodGroup(
                        request.getBloodGroup()
                );

        categoryRepository
                .findByBloodGroupIgnoreCase(
                        bloodGroup
                )
                .filter(existingCategory ->
                        !existingCategory
                                .getId()
                                .equals(categoryId)
                )
                .ifPresent(existingCategory -> {
                    throw new DuplicateResourceException(
                            "Blood category already exists: "
                                    + bloodGroup
                    );
                });

        category.setBloodGroup(bloodGroup);

        category.setDescription(
                normalizeDescription(
                        request.getDescription()
                )
        );

        BloodCategory updatedCategory =
                categoryRepository.save(category);

        return toResponse(updatedCategory);
    }

    /*
     * Category-ga ACTIVE ama INACTIVE ka dhig.
     *
     * CategoryStatusRequest lama isticmaalayo.
     * Enum-ka ayaa method-ka si toos ah u imaanaya.
     */
    @Transactional
    public CategoryResponse updateCategoryStatus(
            Long categoryId,
            CategoryStatus status
    ) {

        if (status == null) {
            throw new BadRequestException(
                    "Category status is required"
            );
        }

        BloodCategory category =
                findCategoryEntityById(categoryId);

        if (category.getStatus() == status) {
            throw new BadRequestException(
                    "Category status is already "
                            + status
            );
        }

        category.setStatus(status);

        BloodCategory updatedCategory =
                categoryRepository.save(category);

        return toResponse(updatedCategory);
    }

    /*
     * Entity-ga u beddel Response DTO.
     */
    private CategoryResponse toResponse(
            BloodCategory category
    ) {

        return CategoryResponse.builder()
                .id(category.getId())
                .bloodGroup(category.getBloodGroup())
                .description(category.getDescription())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .build();
    }

    /*
     * Blood group-ka nadiifi oo uppercase ka dhig.
     */
    private String normalizeBloodGroup(
            String bloodGroup
    ) {

        if (bloodGroup == null ||
                bloodGroup.isBlank()) {

            throw new BadRequestException(
                    "Blood group is required"
            );
        }

        return bloodGroup
                .trim()
                .toUpperCase(Locale.ROOT);
    }

    /*
     * Description-ka nadiifi.
     */
    private String normalizeDescription(
            String description
    ) {

        if (description == null ||
                description.isBlank()) {
            return null;
        }

        return description
                .trim()
                .replaceAll("\\s+", " ");
    }
}