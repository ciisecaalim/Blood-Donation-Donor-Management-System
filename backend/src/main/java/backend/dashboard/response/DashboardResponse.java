package backend.dashboard.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalDonors;

    private long totalDonations;

    private long totalBloodUnits;

    private long totalAnnouncements;

    private long activeAnnouncements;

    private long availableGroups;

    private long lowStockGroups;

    private long outOfStockGroups;
}