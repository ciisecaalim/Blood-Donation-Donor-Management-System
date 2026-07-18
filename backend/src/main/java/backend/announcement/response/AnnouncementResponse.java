package backend.announcement.response;


import backend.common.enums.AnnouncementStatus;
import backend.common.enums.UrgencyLevel;

import java.time.LocalDateTime;


public class AnnouncementResponse {


    private Long id;


    private String title;


    private String bloodGroup;


    private Integer quantityNeeded;


    private UrgencyLevel urgency;


    private String location;


    private String description;


    private AnnouncementStatus status;


    private LocalDateTime createdAt;



    // Default constructor
    public AnnouncementResponse() {
    }



    // Constructor
    public AnnouncementResponse(
            Long id,
            String title,
            String bloodGroup,
            Integer quantityNeeded,
            UrgencyLevel urgency,
            String location,
            String description,
            AnnouncementStatus status,
            LocalDateTime createdAt
    ) {

        this.id = id;
        this.title = title;
        this.bloodGroup = bloodGroup;
        this.quantityNeeded = quantityNeeded;
        this.urgency = urgency;
        this.location = location;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }




    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }



    public void setTitle(String title) {
        this.title = title;
    }



    public String getBloodGroup() {
        return bloodGroup;
    }


    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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



    public AnnouncementStatus getStatus() {
        return status;
    }


    public void setStatus(AnnouncementStatus status) {
        this.status = status;
    }



    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}