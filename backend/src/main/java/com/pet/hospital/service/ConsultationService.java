package com.pet.hospital.service;

import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsultationService {

    @Autowired
    private DataStore dataStore;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private ConsumableService consumableService;

    @Autowired
    private ReminderService reminderService;

    public List<Consultation> getAllConsultations() {
        return new ArrayList<>(dataStore.getConsultations().values());
    }

    public Consultation getConsultationById(Long id) {
        return dataStore.getConsultations().get(id);
    }

    public List<Consultation> getConsultationsByOwnerId(Long ownerId) {
        return dataStore.getConsultations().values().stream()
                .filter(c -> c.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public List<Consultation> getConsultationsByDoctorId(Long doctorId) {
        return dataStore.getConsultations().values().stream()
                .filter(c -> c.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    public Consultation createConsultation(Consultation consultation) {
        if (!doctorService.checkDoctorAuthorization(consultation.getDoctorId(), consultation.getTreatmentItemIds().get(0))) {
            throw new RuntimeException("医生无此诊疗项目权限");
        }

        LocalDateTime time = consultation.getConsultationTime();
        if (!doctorService.checkDoctorSchedule(consultation.getDoctorId(),
                time.toLocalDate(), time.toLocalTime())) {
            throw new RuntimeException("医生当前无排班");
        }

        if (consultation.getScheduleId() != null) {
            doctorService.bookSchedule(consultation.getScheduleId());
        }

        Long id = dataStore.generateId();
        consultation.setId(id);
        consultation.setCode(dataStore.generateCode("CONS"));
        consultation.setStatus("IN_PROGRESS");
        dataStore.getConsultations().put(id, consultation);

        return consultation;
    }

    public Consultation updateConsultation(Long id, Consultation consultation) {
        if (!dataStore.getConsultations().containsKey(id)) {
            return null;
        }
        consultation.setId(id);
        dataStore.getConsultations().put(id, consultation);
        return consultation;
    }

    public Consultation completeConsultation(Long consultationId) {
        Consultation consultation = dataStore.getConsultations().get(consultationId);
        if (consultation == null) {
            return null;
        }

        consultation.setStatus("COMPLETED");

        Boolean needFollowUp = consultation.getNeedFollowUp();
        if (needFollowUp == null) {
            needFollowUp = false;
            for (Long treatmentId : consultation.getTreatmentItemIds()) {
                TreatmentItem item = dataStore.getTreatmentItems().get(treatmentId);
                if (item != null && item.getNeedFollowUp()) {
                    needFollowUp = true;
                    break;
                }
            }
            consultation.setNeedFollowUp(needFollowUp);
        }

        for (Long treatmentId : consultation.getTreatmentItemIds()) {
            TreatmentItem item = dataStore.getTreatmentItems().get(treatmentId);
            if (item != null) {
                if (item.getIsVaccine()) {
                    reminderService.createVaccineReminder(consultation, item);
                }
                if (item.getNeedFollowUp()) {
                    reminderService.createFollowUpReminder(consultation, item);
                }
            }
        }

        return consultation;
    }
}
