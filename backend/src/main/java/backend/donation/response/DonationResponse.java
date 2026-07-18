package backend.donation.response;

import backend.common.enums.DonationStatus;

import java.time.LocalDate;


public class DonationResponse {


    private Long id;

    // Donor name
    private String donorName;

    // Blood group
    private String bloodGroup;

    private Integer quantity;

    private LocalDate donationDate;

    private String location;

    private DonationStatus status;

    private String notes;



    // Default constructor
    public DonationResponse() {
    }



    // Constructor
    public DonationResponse(
            Long id,
            String donorName,
            String bloodGroup,
            Integer quantity,
            LocalDate donationDate,
            String location,
            DonationStatus status,
            String notes
    ) {
        this.id = id;
        this.donorName = donorName;
        this.bloodGroup = bloodGroup;
        this.quantity = quantity;
        this.donationDate = donationDate;
        this.location = location;
        this.status = status;
        this.notes = notes;
    }



    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }



    public String getDonorName() {
        return donorName;
    }


    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }



    public String getBloodGroup() {
        return bloodGroup;
    }


    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }



    public Integer getQuantity() {
        return quantity;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }



    public LocalDate getDonationDate() {
        return donationDate;
    }


    public void setDonationDate(LocalDate donationDate) {
        this.donationDate = donationDate;
    }



    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }



    public DonationStatus getStatus() {
        return status;
    }


    public void setStatus(DonationStatus status) {
        this.status = status;
    }



    public String getNotes() {
        return notes;
    }


    public void setNotes(String notes) {
        this.notes = notes;
    }
}