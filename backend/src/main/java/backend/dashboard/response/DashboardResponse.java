package backend.dashboard.response;


import lombok.Getter;


@Getter
public class DashboardResponse {


    private long totalDonors;

    private long totalBloodRequests;

    private long totalBloodUnits;


    public DashboardResponse(long totalDonors,
                             long totalBloodRequests,
                             long totalBloodUnits) {

        this.totalDonors = totalDonors;
        this.totalBloodRequests = totalBloodRequests;
        this.totalBloodUnits = totalBloodUnits;
    }

}