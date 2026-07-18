package backend.donation.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class DonationRequest {


    @NotNull(message = "Donor is required")
    private Long donorId;


    @NotNull(message = "Blood category is required")
    private Long categoryId;


    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;


    @NotNull(message = "Donation date is required")
    private LocalDate donationDate;


    @NotBlank(message = "Location is required")
    @Size(max = 120, message = "Location cannot exceed 120 characters")
    private String location;


    @Size(max = 250, message = "Notes cannot exceed 250 characters")
    private String notes;



    // Default constructor
    public DonationRequest() {
    }



    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}