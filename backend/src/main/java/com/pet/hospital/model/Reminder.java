package com.pet.hospital.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reminder {
    private Long id;
    private String code;
    private String type;
    private Long ownerId;
    private String ownerName;
    private String ownerPhone;
    private Long petId;
    private String petName;
    private Long relatedId;
    private String relatedCode;
    private LocalDate remindDate;
    private String content;
    private String status;
    private LocalDateTime sentTime;
    private String notes;
    private LocalDateTime createTime;

    public Reminder() {
        this.createTime = LocalDateTime.now();
        this.status = "PENDING";
    }

    public static final String TYPE_FOLLOW_UP = "FOLLOW_UP";
    public static final String TYPE_VACCINE = "VACCINE";
    public static final String TYPE_BOARDING_OVERDUE = "BOARDING_OVERDUE";

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_SENT = "SENT";
    public static final String STATUS_CANCELLED = "CANCELLED";

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerPhone() { return ownerPhone; }
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }
    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }
    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }
    public Long getRelatedId() { return relatedId; }
    public void setRelatedId(Long relatedId) { this.relatedId = relatedId; }
    public String getRelatedCode() { return relatedCode; }
    public void setRelatedCode(String relatedCode) { this.relatedCode = relatedCode; }
    public LocalDate getRemindDate() { return remindDate; }
    public void setRemindDate(LocalDate remindDate) { this.remindDate = remindDate; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getSentTime() { return sentTime; }
    public void setSentTime(LocalDateTime sentTime) { this.sentTime = sentTime; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
