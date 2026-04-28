package com.pet.hospital.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Medicine {
    private Long id;
    private String code;
    private String name;
    private String specification;
    private String unit;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private String category;
    private String description;
    private String status;
    private LocalDate createTime;

    public Medicine() {
        this.createTime = LocalDate.now();
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getCreateTime() { return createTime; }
    public void setCreateTime(LocalDate createTime) { this.createTime = createTime; }
}
