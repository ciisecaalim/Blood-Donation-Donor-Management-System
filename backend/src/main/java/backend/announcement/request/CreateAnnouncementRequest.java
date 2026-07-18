package backend.announcement.request;


import backend.common.enums.UrgencyLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class CreateAnnouncementRequest {


    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;



    @NotNull(message = "Blood category is required")
    private Long categoryId;



    @NotNull(message = "Quantity needed is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantityNeeded;



    @NotNull(message = "Urgency is required")
    private UrgencyLevel urgency;



    @NotBlank(message = "Location is required")
    @Size(max = 120, message = "Location cannot exceed 120 characters")
    private String location;



    @Size(max = 300, message = "Description cannot exceed 300 characters")
    private String description;



    public CreateAnnouncementRequest() {
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }



    public Integer getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(Integer quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }



    public UrgencyLevel getUrgency() {
        return urgency;
    }

    public void setUrgency(UrgencyLevel urgency) {
        this.urgency = urgency;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}