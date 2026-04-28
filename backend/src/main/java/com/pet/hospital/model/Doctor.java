package com.pet.hospital.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Doctor {
    private Long id;
    private String name;
    private String title;
    private Long departmentId;
    private String departmentName;
    private String phone;
    private List<Long> authorizedTreatmentIds = new ArrayList<>();
    private String status;
    private LocalDate createTime;
    private String notes;

    public Doctor() {
        this.createTime = LocalDate.now();
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<Long> getAuthorizedTreatmentIds() { return authorizedTreatmentIds; }
    public void setAuthorizedTreatmentIds(List<Long> authorizedTreatmentIds) { this.authorizedTreatmentIds = authorizedTreatmentIds; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getCreateTime() { return createTime; }
    public void setCreateTime(LocalDate createTime) { this.createTime = createTime; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
