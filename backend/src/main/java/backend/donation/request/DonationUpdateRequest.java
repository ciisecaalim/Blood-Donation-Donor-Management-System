package backend.donation.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DonationUpdateRequest {


    // Updated blood quantity
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;


    // Updated donation location
    @Size(max = 120, message = "Location cannot exceed 120 characters")
    private String location;


    // Updated notes
    @Size(max = 250, message = "Notes cannot exceed 250 characters")
    private String notes;



    // Default constructor
    public DonationUpdateRequest() {
    }



    public Integer getQuantity() {
        return quantity;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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