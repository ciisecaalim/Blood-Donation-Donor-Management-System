package backend.report.response;

import backend.common.enums.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * Qaybta inventory-ga ee complete system report.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReportSection {

    /*
     * Wadarta blood units-ka inventory-ga ku jira.
     */
    private long totalBloodUnits;

    /*
     * Tirada blood groups-ka AVAILABLE ah.
     */
    private long availableGroups;

    /*
     * Tirada blood groups-ka LOW_STOCK ah.
     */
    private long lowStockGroups;

    /*
     * Tirada blood groups-ka OUT_OF_STOCK ah.
     */
    private long outOfStockGroups;

    /*
     * Liiska inventory records-ka.
     */
    private List<InventoryItem> records;

    /*
     * Hal row oo inventory report-ka ka mid ah.
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryItem {

        private Long id;

        private String bloodGroup;

        private Integer quantity;

        private InventoryStatus status;
    }
}