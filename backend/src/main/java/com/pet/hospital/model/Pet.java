package com.pet.hospital.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Pet {
    private Long id;
    private String name;
    private String breed;
    private Integer age;
    private BigDecimal weight;
    private String gender;
    private List<String> allergicMedicines = new ArrayList<>();
    private Long ownerId;
    private LocalDate createTime;
    private String notes;

    public Pet() {
        this.createTime = LocalDate.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public List<String> getAllergicMedicines() { return allergicMedicines; }
    public void setAllergicMedicines(List<String> allergicMedicines) { this.allergicMedicines = allergicMedicines; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public LocalDate getCreateTime() { return createTime; }
    public void setCreateTime(LocalDate createTime) { this.createTime = createTime; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
