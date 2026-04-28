package com.pet.hospital.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Boarding {
    private Long id;
    private String code;
    private Long petId;
    private String petName;
    private BigDecimal petWeight;
    private Long ownerId;
    private String ownerName;
    private String ownerPhone;
    private Long cageId;
    private String cageName;
    private BigDecimal dailyRate;
    private LocalDate checkInDate;
    private LocalDate expectedCheckOutDate;
    private LocalDate actualCheckOutDate;
    private Integer days;
    private BigDecimal totalAmount;
    private BigDecimal extraDaysAmount;
    private BigDecimal totalPayable;
    private String status;
    private String notes;
    private Long billId;
    private LocalDateTime createTime;

    public Boarding() {
        this.createTime = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }
    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }
    public BigDecimal getPetWeight() { return petWeight; }
    public void setPetWeight(BigDecimal petWeight) { this.petWeight = petWeight; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerPhone() { return ownerPhone; }
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }
    public Long getCageId() { return cageId; }
    public void setCageId(Long cageId) { this.cageId = cageId; }
    public String getCageName() { return cageName; }
    public void setCageName(String cageName) { this.cageName = cageName; }
    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public LocalDate getExpectedCheckOutDate() { return expectedCheckOutDate; }
    public void setExpectedCheckOutDate(LocalDate expectedCheckOutDate) { this.expectedCheckOutDate = expectedCheckOutDate; }
    public LocalDate getActualCheckOutDate() { return actualCheckOutDate; }
    public void setActualCheckOutDate(LocalDate actualCheckOutDate) { this.actualCheckOutDate = actualCheckOutDate; }
    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getExtraDaysAmount() { return extraDaysAmount; }
    public void setExtraDaysAmount(BigDecimal extraDaysAmount) { this.extraDaysAmount = extraDaysAmount; }
    public BigDecimal getTotalPayable() { return totalPayable; }
    public void setTotalPayable(BigDecimal totalPayable) { this.totalPayable = totalPayable; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
