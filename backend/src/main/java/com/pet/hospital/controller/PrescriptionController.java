package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.Prescription;
import com.pet.hospital.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping
    public Result<List<Prescription>> getAllPrescriptions() {
        return Result.success(prescriptionService.getAllPrescriptions());
    }

    @GetMapping("/{id}")
    public Result<Prescription> getPrescriptionById(@PathVariable Long id) {
        Prescription prescription = prescriptionService.getPrescriptionById(id);
        if (prescription == null) {
            return Result.error("处方不存在");
        }
        return Result.success(prescription);
    }

    @GetMapping("/owner/{ownerId}")
    public Result<List<Prescription>> getPrescriptionsByOwnerId(@PathVariable Long ownerId) {
        return Result.success(prescriptionService.getPrescriptionsByOwnerId(ownerId));
    }

    @PostMapping
    public Result<Prescription> createPrescription(@RequestBody Prescription prescription) {
        try {
            Prescription created = prescriptionService.createPrescription(prescription);
            return Result.success(created);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Prescription> updatePrescription(@PathVariable Long id, @RequestBody Prescription prescription) {
        Prescription updated = prescriptionService.updatePrescription(id, prescription);
        if (updated == null) {
            return Result.error("处方不存在");
        }
        return Result.success(updated);
    }

    @PostMapping("/{id}/dispense")
    public Result<Map<Long, Map<Long, Integer>>> dispensePrescription(@PathVariable Long id) {
        try {
            Map<Long, Map<Long, Integer>> dispensed = prescriptionService.dispensePrescription(id);
            return Result.success(dispensed);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    public Result<Prescription> cancelPrescription(@PathVariable Long id) {
        Prescription cancelled = prescriptionService.cancelPrescription(id);
        if (cancelled == null) {
            return Result.error("处方不存在");
        }
        return Result.success(cancelled);
    }
}
