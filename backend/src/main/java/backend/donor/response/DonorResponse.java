


package backend.donor.response;

import backend.common.enums.DonorStatus;
import backend.common.enums.Gender;

import java.time.LocalDateTime;

public class DonorResponse {

    private Long id;
    private String fullName;
    private String bloodGroup;
    private String phone;
    private String address;
    private Integer age;
    private Gender gender;
    private Double weight;
    private DonorStatus status;
    private LocalDateTime createdAt;

    public DonorResponse() {
    }

    public DonorResponse(Long id, String fullName, String bloodGroup, String phone,
                         String address, Integer age, Gender gender,
                         Double weight, DonorStatus status,
                         LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public DonorStatus getStatus() {
        return status;
    }

    public void setStatus(DonorStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}