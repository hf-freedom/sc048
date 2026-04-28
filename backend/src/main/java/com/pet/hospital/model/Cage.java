package com.pet.hospital.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Cage {
    private Long id;
    private String code;
    private String name;
    private String type;
    private Integer maxWeight;
    private BigDecimal dailyRate;
    private String status;
    private LocalDate createTime;

    public Cage() {
        this.createTime = LocalDate.now();
        this.status = "AVAILABLE";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getMaxWeight() { return maxWeight; }
    public void setMaxWeight(Integer maxWeight) { this.maxWeight = maxWeight; }
    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getCreateTime() { return createTime; }
    public void setCreateTime(LocalDate createTime) { this.createTime = createTime; }
}
