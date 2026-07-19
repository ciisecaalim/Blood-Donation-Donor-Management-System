package backend.report.response;

public class BloodGroupReportResponse {

    private String bloodGroup;
    private Long totalUnits;

    public BloodGroupReportResponse(String bloodGroup, Long totalUnits) {
        this.bloodGroup = bloodGroup;
        this.totalUnits = totalUnits;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Long getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(Long totalUnits) {
        this.totalUnits = totalUnits;
    }
}