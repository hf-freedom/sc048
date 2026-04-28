package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.Doctor;
import com.pet.hospital.model.Schedule;
import com.pet.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public Result<List<Doctor>> getAllDoctors() {
        return Result.success(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public Result<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return Result.error("医生不存在");
        }
        return Result.success(doctor);
    }

    @PostMapping
    public Result<Doctor> createDoctor(@RequestBody Doctor doctor) {
        return Result.success(doctorService.createDoctor(doctor));
    }

    @PutMapping("/{id}")
    public Result<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        Doctor updated = doctorService.updateDoctor(id, doctor);
        if (updated == null) {
            return Result.error("医生不存在");
        }
        return Result.success(updated);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDoctor(@PathVariable Long id) {
        boolean deleted = doctorService.deleteDoctor(id);
        if (!deleted) {
            return Result.error("医生不存在");
        }
        return Result.success(true);
    }

    @GetMapping("/{id}/check-authorization")
    public Result<Boolean> checkDoctorAuthorization(@PathVariable Long id, @RequestParam Long treatmentItemId) {
        boolean authorized = doctorService.checkDoctorAuthorization(id, treatmentItemId);
        return Result.success(authorized);
    }

    @GetMapping("/{id}/check-schedule")
    public Result<Boolean> checkDoctorSchedule(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        boolean available = doctorService.checkDoctorSchedule(id, date, time);
        return Result.success(available);
    }

    @GetMapping("/{id}/schedules")
    public Result<List<Schedule>> getDoctorSchedules(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(doctorService.getDoctorSchedules(id, startDate, endDate));
    }

    @GetMapping("/schedules/available")
    public Result<List<Schedule>> getAvailableSchedules(
            @RequestParam(required = false) Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(doctorService.getAvailableSchedules(departmentId, startDate, endDate));
    }

    @PostMapping("/schedules")
    public Result<Schedule> createSchedule(@RequestBody Schedule schedule) {
        return Result.success(doctorService.createSchedule(schedule));
    }

    @PutMapping("/schedules/{id}")
    public Result<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        Schedule updated = doctorService.updateSchedule(id, schedule);
        if (updated == null) {
            return Result.error("排班不存在");
        }
        return Result.success(updated);
    }

    @PostMapping("/schedules/{id}/cancel")
    public Result<Boolean> cancelSchedule(@PathVariable Long id) {
        boolean cancelled = doctorService.cancelSchedule(id);
        if (!cancelled) {
            return Result.error("排班不存在");
        }
        return Result.success(true);
    }
}
