package backend.category.config;

import backend.category.Model.BloodCategory;
import backend.category.repository.CategoryRepository;
import backend.common.enums.CategoryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CategoryDataInitializer
        implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) {

        createCategoryIfNotExists(
                "A+",
                "A positive blood group"
        );

        createCategoryIfNotExists(
                "A-",
                "A negative blood group"
        );

        createCategoryIfNotExists(
                "B+",
                "B positive blood group"
        );

        createCategoryIfNotExists(
                "B-",
                "B negative blood group"
        );

        createCategoryIfNotExists(
                "AB+",
                "AB positive blood group"
        );

        createCategoryIfNotExists(
                "AB-",
                "AB negative blood group"
        );

        createCategoryIfNotExists(
                "O+",
                "O positive blood group"
        );

        createCategoryIfNotExists(
                "O-",
                "O negative blood group"
        );
    }

    private void createCategoryIfNotExists(
            String bloodGroup,
            String description
    ) {

        boolean exists =
                categoryRepository
                        .existsByBloodGroupIgnoreCase(
                                bloodGroup
                        );

        if (!exists) {

            BloodCategory category =
                    BloodCategory.builder()
                            .bloodGroup(bloodGroup)
                            .description(description)
                            .status(CategoryStatus.ACTIVE)
                            .build();

            categoryRepository.save(category);
        }
    }
}