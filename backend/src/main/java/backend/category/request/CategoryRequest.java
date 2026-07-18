package backend.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Blood group is required")
    @Pattern(
            regexp = "(?i)^(A|B|AB|O)[+-]$",
            message = "Blood group must be A+, A-, B+, B-, AB+, AB-, O+, or O-"
    )
    private String bloodGroup;

    @Size(
            max = 150,
            message = "Description cannot exceed 150 characters"
    )
    private String description;
}