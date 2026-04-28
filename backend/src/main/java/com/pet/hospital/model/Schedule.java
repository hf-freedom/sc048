package com.pet.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private Long id;
    private String code;
    private Long doctorId;
    private String doctorName;
    private Long departmentId;
    private String departmentName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxPatients;
    private Integer bookedPatients;
    private String status;
    private LocalDate createTime;

    public Schedule() {
        this.createTime = LocalDate.now();
        this.status = "ACTIVE";
        this.bookedPatients = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public Integer getMaxPatients() { return maxPatients; }
    public void setMaxPatients(Integer maxPatients) { this.maxPatients = maxPatients; }
    public Integer getBookedPatients() { return bookedPatients; }
    public void setBookedPatients(Integer bookedPatients) { this.bookedPatients = bookedPatients; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getCreateTime() { return createTime; }
    public void setCreateTime(LocalDate createTime) { this.createTime = createTime; }
}
