package com.pet.hospital.model;

import java.math.BigDecimal;

public enum MemberLevel {
    NORMAL("普通会员", 1.00, 1.00),
    SILVER("银卡会员", 0.90, 0.95),
    GOLD("金卡会员", 0.80, 0.90),
    PLATINUM("铂金会员", 0.70, 0.85);

    private String description;
    private double treatmentDiscount;
    private double medicineDiscount;

    MemberLevel(String description, double treatmentDiscount, double medicineDiscount) {
        this.description = description;
        this.treatmentDiscount = treatmentDiscount;
        this.medicineDiscount = medicineDiscount;
    }

    public String getDescription() { return description; }
    public BigDecimal getTreatmentDiscount() { return BigDecimal.valueOf(treatmentDiscount); }
    public BigDecimal getMedicineDiscount() { return BigDecimal.valueOf(medicineDiscount); }
}
