package com.pet.hospital.service;

import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DataStore dataStore;

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(dataStore.getDoctors().values());
    }

    public Doctor getDoctorById(Long id) {
        return dataStore.getDoctors().get(id);
    }

    public Doctor createDoctor(Doctor doctor) {
        Long id = dataStore.generateId();
        doctor.setId(id);
        dataStore.getDoctors().put(id, doctor);
        return doctor;
    }

    public Doctor updateDoctor(Long id, Doctor doctor) {
        if (!dataStore.getDoctors().containsKey(id)) {
            return null;
        }
        doctor.setId(id);
        dataStore.getDoctors().put(id, doctor);
        return doctor;
    }

    public boolean deleteDoctor(Long id) {
        return dataStore.getDoctors().remove(id) != null;
    }

    public boolean checkDoctorAuthorization(Long doctorId, Long treatmentItemId) {
        Doctor doctor = dataStore.getDoctors().get(doctorId);
        if (doctor == null) {
            return false;
        }
        return doctor.getAuthorizedTreatmentIds().contains(treatmentItemId);
    }

    public boolean checkDoctorSchedule(Long doctorId, LocalDate date, LocalTime time) {
        return dataStore.getSchedules().values().stream()
                .anyMatch(s -> s.getDoctorId().equals(doctorId)
                        && s.getDate().equals(date)
                        && !time.isBefore(s.getStartTime())
                        && !time.isAfter(s.getEndTime())
                        && "ACTIVE".equals(s.getStatus()));
    }

    public List<Schedule> getDoctorSchedules(Long doctorId, LocalDate startDate, LocalDate endDate) {
        return dataStore.getSchedules().values().stream()
                .filter(s -> s.getDoctorId().equals(doctorId))
                .filter(s -> !s.getDate().isBefore(startDate) && !s.getDate().isAfter(endDate))
                .sorted(Comparator.comparing(Schedule::getDate).thenComparing(Schedule::getStartTime))
                .collect(Collectors.toList());
    }

    public List<Schedule> getAvailableSchedules(Long departmentId, LocalDate startDate, LocalDate endDate) {
        return dataStore.getSchedules().values().stream()
                .filter(s -> departmentId == null || s.getDepartmentId().equals(departmentId))
                .filter(s -> !s.getDate().isBefore(startDate) && !s.getDate().isAfter(endDate))
                .filter(s -> s.getBookedPatients() < s.getMaxPatients())
                .filter(s -> "ACTIVE".equals(s.getStatus()))
                .sorted(Comparator.comparing(Schedule::getDate).thenComparing(Schedule::getStartTime))
                .collect(Collectors.toList());
    }

    public Schedule createSchedule(Schedule schedule) {
        Long id = dataStore.generateId();
        schedule.setId(id);
        schedule.setCode(dataStore.generateCode("SCH"));
        dataStore.getSchedules().put(id, schedule);
        return schedule;
    }

    public Schedule updateSchedule(Long id, Schedule schedule) {
        if (!dataStore.getSchedules().containsKey(id)) {
            return null;
        }
        schedule.setId(id);
        dataStore.getSchedules().put(id, schedule);
        return schedule;
    }

    public boolean cancelSchedule(Long id) {
        Schedule schedule = dataStore.getSchedules().get(id);
        if (schedule == null) {
            return false;
        }
        schedule.setStatus("CANCELLED");
        return true;
    }

    public boolean bookSchedule(Long scheduleId) {
        Schedule schedule = dataStore.getSchedules().get(scheduleId);
        if (schedule == null || schedule.getBookedPatients() >= schedule.getMaxPatients()
                || !"ACTIVE".equals(schedule.getStatus())) {
            return false;
        }
        schedule.setBookedPatients(schedule.getBookedPatients() + 1);
        return true;
    }
}
