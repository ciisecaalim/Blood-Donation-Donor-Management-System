package backend.inventory.response;

import backend.common.enums.InventoryStatus;

/**
 * Response DTO returned to the frontend.
 *
 * This class sends inventory information
 * back to React. We use a Response DTO
 * instead of returning the Entity directly.
 */
public class InventoryResponse {

    /**
     * Inventory ID.
     */
    private Long id;

    /**
     * Blood group name.
     * Example: A+, O-, AB+
     */
    private String bloodGroup;

    /**
     * Available blood units.
     */
    private Integer quantity;

    /**
     * Current inventory status.
     * AVAILABLE
     * LOW_STOCK
     * OUT_OF_STOCK
     */
    private InventoryStatus status;

    /**
     * Additional notes.
     */
    private String notes;

    // Default constructor
    public InventoryResponse() {
    }

    // Constructor with all fields
    public InventoryResponse(Long id,
                             String bloodGroup,
                             Integer quantity,
                             InventoryStatus status,
                             String notes) {
        this.id = id;
        this.bloodGroup = bloodGroup;
        this.quantity = quantity;
        this.status = status;
        this.notes = notes;
    }

    // Returns inventory ID
    public Long getId() {
        return id;
    }

    // Sets inventory ID
    public void setId(Long id) {
        this.id = id;
    }

    // Returns blood group
    public String getBloodGroup() {
        return bloodGroup;
    }

    // Sets blood group
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    // Returns quantity
    public Integer getQuantity() {
        return quantity;
    }

    // Sets quantity
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Returns inventory status
    public InventoryStatus getStatus() {
        return status;
    }

    // Sets inventory status
    public void setStatus(InventoryStatus status) {
        this.status = status;
    }

    // Returns notes
    public String getNotes() {
        return notes;
    }

    // Sets notes
    public void setNotes(String notes) {
        this.notes = notes;
    }
}