package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.*;
import com.pet.hospital.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "*")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private BillService billService;

    @GetMapping
    public Result<List<Consultation>> getAllConsultations() {
        return Result.success(consultationService.getAllConsultations());
    }

    @GetMapping("/{id}")
    public Result<Consultation> getConsultationById(@PathVariable Long id) {
        Consultation consultation = consultationService.getConsultationById(id);
        if (consultation == null) {
            return Result.error("就诊记录不存在");
        }
        return Result.success(consultation);
    }

    @GetMapping("/owner/{ownerId}")
    public Result<List<Consultation>> getConsultationsByOwnerId(@PathVariable Long ownerId) {
        return Result.success(consultationService.getConsultationsByOwnerId(ownerId));
    }

    @GetMapping("/doctor/{doctorId}")
    public Result<List<Consultation>> getConsultationsByDoctorId(@PathVariable Long doctorId) {
        return Result.success(consultationService.getConsultationsByDoctorId(doctorId));
    }

    @PostMapping
    public Result<Consultation> createConsultation(@RequestBody Consultation consultation) {
        try {
            Consultation created = consultationService.createConsultation(consultation);
            return Result.success(created);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Consultation> updateConsultation(@PathVariable Long id, @RequestBody Consultation consultation) {
        Consultation updated = consultationService.updateConsultation(id, consultation);
        if (updated == null) {
            return Result.error("就诊记录不存在");
        }
        return Result.success(updated);
    }

    @PostMapping("/{id}/complete")
    public Result<Consultation> completeConsultation(@PathVariable Long id) {
        Consultation completed = consultationService.completeConsultation(id);
        if (completed == null) {
            return Result.error("就诊记录不存在");
        }
        return Result.success(completed);
    }

    @PostMapping("/{id}/prescription")
    public Result<Prescription> createPrescription(@PathVariable Long id, @RequestBody Prescription prescription) {
        Consultation consultation = consultationService.getConsultationById(id);
        if (consultation == null) {
            return Result.error("就诊记录不存在");
        }
        prescription.setConsultationId(id);
        prescription.setPetId(consultation.getPetId());
        prescription.setOwnerId(consultation.getOwnerId());
        prescription.setDoctorId(consultation.getDoctorId());
        try {
            Prescription created = prescriptionService.createPrescription(prescription);
            consultation.setPrescriptionId(created.getId());
            return Result.success(created);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/bill")
    public Result<Bill> createBill(@PathVariable Long id) {
        Consultation consultation = consultationService.getConsultationById(id);
        if (consultation == null) {
            return Result.error("就诊记录不存在");
        }
        Prescription prescription = null;
        if (consultation.getPrescriptionId() != null) {
            prescription = prescriptionService.getPrescriptionById(consultation.getPrescriptionId());
        }
        Bill bill = billService.createConsultationBill(consultation, prescription);
        consultation.setBillId(bill.getId());
        return Result.success(bill);
    }
}
