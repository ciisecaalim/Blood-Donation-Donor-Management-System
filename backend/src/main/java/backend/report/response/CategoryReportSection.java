package backend.report.response;

import backend.common.enums.CategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Qaybta blood categories-ka ee system report-ka.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryReportSection {

    /*
     * Tirada guud ee categories-ka.
     */
    private long totalCategories;

    /*
     * Tirada ACTIVE categories-ka.
     */
    private long activeCategories;

    /*
     * Tirada INACTIVE categories-ka.
     */
    private long inactiveCategories;

    /*
     * Liiska blood categories-ka.
     */
    private List<CategoryItem> records;

    /*
     * Hal row oo category report-ka ka mid ah.
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryItem {

        private Long id;

        private String bloodGroup;

        private String description;

        private CategoryStatus status;

        private LocalDateTime createdAt;
    }
}