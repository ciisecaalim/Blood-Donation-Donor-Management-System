package backend.inventory.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO used when creating a new inventory record.
 *
 * This class receives data sent from the frontend (React).
 * It does NOT represent the database table.
 */
public class InventoryRequest {

    /**
     * Blood category ID selected from the blood group dropdown.
     * Example: A+, B+, O-
     */
    @NotNull(message = "Blood category is required")
    private Long categoryId;

    /**
     * Number of blood units available.
     * Cannot be negative.
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    /**
     * Optional notes about this inventory record.
     */
    @Size(max = 200, message = "Notes cannot exceed 200 characters")
    private String notes;

    // Default constructor
    public InventoryRequest() {
    }

    // Returns the blood category ID
    public Long getCategoryId() {
        return categoryId;
    }

    // Sets the blood category ID
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // Returns the quantity
    public Integer getQuantity() {
        return quantity;
    }

    // Sets the quantity
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Returns the notes
    public String getNotes() {
        return notes;
    }

    // Sets the notes
    public void setNotes(String notes) {
        this.notes = notes;
    }
}