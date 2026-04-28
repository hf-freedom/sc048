package com.pet.hospital.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class TreatmentItem {
    private Long id;
    private String code;
    private String name;
    private Long departmentId;
    private String departmentName;
    private BigDecimal price;
    private BigDecimal cost;
    private List<Long> requiredConsumableIds = new ArrayList<>();
    private Boolean needFollowUp;
    private Integer followUpDays;
    private Boolean isVaccine;
    private Integer nextVaccineDays;
    private String description;
    private String status;
    private LocalDate createTime;

    public TreatmentItem() {
        this.createTime = LocalDate.now();
        this.status = "ACTIVE";
        this.needFollowUp = false;
        this.isVaccine = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    public List<Long> getRequiredConsumableIds() { return requiredConsumableIds; }
    public void setRequiredConsumableIds(List<Long> requiredConsumableIds) { this.requiredConsumableIds = requiredConsumableIds; }
    public Boolean getNeedFollowUp() { return needFollowUp; }
    public void setNeedFollowUp(Boolean needFollowUp) { this.needFollowUp = needFollowUp; }
    public Integer getFollowUpDays() { return followUpDays; }
    public void setFollowUpDays(Integer followUpDays) { this.followUpDays = followUpDays; }
    public Boolean getIsVaccine() { return isVaccine; }
    public void setIsVaccine(Boolean isVaccine) { this.isVaccine = isVaccine; }
    public Integer getNextVaccineDays() { return nextVaccineDays; }
    public void setNextVaccineDays(Integer nextVaccineDays) { this.nextVaccineDays = nextVaccineDays; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getCreateTime() { return createTime; }
    public void setCreateTime(LocalDate createTime) { this.createTime = createTime; }
}
