package backend.category.response;

import backend.common.enums.CategoryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;

    private String bloodGroup;

    private String description;

    private CategoryStatus status;

    private LocalDateTime createdAt;
}