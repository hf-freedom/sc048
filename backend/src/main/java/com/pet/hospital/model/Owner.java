package com.pet.hospital.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Owner {
    private Long id;
    private String name;
    private String phone;
    private String idCard;
    private String address;
    private MemberLevel memberLevel;
    private Integer points;
    private LocalDate createTime;
    private String notes;

    public Owner() {
        this.createTime = LocalDate.now();
        this.points = 0;
        this.memberLevel = MemberLevel.NORMAL;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public MemberLevel getMemberLevel() { return memberLevel; }
    public void setMemberLevel(MemberLevel memberLevel) { this.memberLevel = memberLevel; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    public LocalDate getCreateTime() { return createTime; }
    public void setCreateTime(LocalDate createTime) { this.createTime = createTime; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
