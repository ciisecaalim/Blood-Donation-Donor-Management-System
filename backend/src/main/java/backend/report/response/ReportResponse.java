package backend.report.response;

import lombok.Getter;

@Getter
public class ReportResponse {

    private long totalDonors;

    private long totalDonations;

    private long totalDonatedBlood;

    private long totalBloodAvailable;


    public ReportResponse(long totalDonors,
                          long totalDonations,
                          long totalDonatedBlood,
                          long totalBloodAvailable) {

        this.totalDonors = totalDonors;
        this.totalDonations = totalDonations;
        this.totalDonatedBlood = totalDonatedBlood;
        this.totalBloodAvailable = totalBloodAvailable;
    }
}