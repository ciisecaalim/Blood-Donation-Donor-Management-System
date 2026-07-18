package backend.inventory.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO used when updating an existing inventory record.
 *
 * The user can update:
 * - Quantity
 * - Notes
 *
 * Blood category cannot be changed after the inventory is created.
 */
public class InventoryUpdateRequest {

    /**
     * Updated quantity of blood units.
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    /**
     * Updated notes.
     */
    @Size(max = 200, message = "Notes cannot exceed 200 characters")
    private String notes;

    // Default constructor
    public InventoryUpdateRequest() {
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