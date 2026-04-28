package com.pet.hospital.service;

import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReminderService {

    @Autowired
    private DataStore dataStore;

    public List<Reminder> getAllReminders() {
        return new ArrayList<>(dataStore.getReminders().values());
    }

    public Reminder getReminderById(Long id) {
        return dataStore.getReminders().get(id);
    }

    public List<Reminder> getPendingReminders() {
        List<Reminder> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Reminder reminder : dataStore.getReminders().values()) {
            if (Reminder.STATUS_PENDING.equals(reminder.getStatus())
                    && (reminder.getRemindDate().isEqual(today) || reminder.getRemindDate().isBefore(today))) {
                result.add(reminder);
            }
        }
        return result;
    }

    public Reminder createFollowUpReminder(Consultation consultation, TreatmentItem treatmentItem) {
        Owner owner = dataStore.getOwners().get(consultation.getOwnerId());
        Pet pet = dataStore.getPets().get(consultation.getPetId());

        Reminder reminder = new Reminder();
        Long id = dataStore.generateId();
        reminder.setId(id);
        reminder.setCode(dataStore.generateCode("REM"));
        reminder.setType(Reminder.TYPE_FOLLOW_UP);
        reminder.setOwnerId(consultation.getOwnerId());
        reminder.setOwnerName(owner != null ? owner.getName() : "");
        reminder.setOwnerPhone(owner != null ? owner.getPhone() : "");
        reminder.setPetId(consultation.getPetId());
        reminder.setPetName(pet != null ? pet.getName() : "");
        reminder.setRelatedId(consultation.getId());
        reminder.setRelatedCode(consultation.getCode());

        int followUpDays = treatmentItem.getFollowUpDays() != null ? treatmentItem.getFollowUpDays() : 7;
        reminder.setRemindDate(LocalDate.now().plusDays(followUpDays));
        reminder.setContent("您的宠物【" + (pet != null ? pet.getName() : "") + "】需要复诊，请联系医院预约复诊时间。");
        reminder.setStatus(Reminder.STATUS_PENDING);

        dataStore.getReminders().put(id, reminder);

        return reminder;
    }

    public Reminder createVaccineReminder(Consultation consultation, TreatmentItem treatmentItem) {
        Owner owner = dataStore.getOwners().get(consultation.getOwnerId());
        Pet pet = dataStore.getPets().get(consultation.getPetId());

        Reminder reminder = new Reminder();
        Long id = dataStore.generateId();
        reminder.setId(id);
        reminder.setCode(dataStore.generateCode("REM"));
        reminder.setType(Reminder.TYPE_VACCINE);
        reminder.setOwnerId(consultation.getOwnerId());
        reminder.setOwnerName(owner != null ? owner.getName() : "");
        reminder.setOwnerPhone(owner != null ? owner.getPhone() : "");
        reminder.setPetId(consultation.getPetId());
        reminder.setPetName(pet != null ? pet.getName() : "");
        reminder.setRelatedId(consultation.getId());
        reminder.setRelatedCode(consultation.getCode());

        int nextVaccineDays = treatmentItem.getNextVaccineDays() != null ? treatmentItem.getNextVaccineDays() : 365;
        reminder.setRemindDate(LocalDate.now().plusDays(nextVaccineDays));
        reminder.setContent("您的宠物【" + (pet != null ? pet.getName() : "") + "】的【" + treatmentItem.getName() + "】疫苗即将到期，请及时接种。");
        reminder.setStatus(Reminder.STATUS_PENDING);

        dataStore.getReminders().put(id, reminder);

        return reminder;
    }

    public Reminder createOverdueReminder(Boarding boarding) {
        Owner owner = dataStore.getOwners().get(boarding.getOwnerId());
        Pet pet = dataStore.getPets().get(boarding.getPetId());

        Reminder reminder = new Reminder();
        Long id = dataStore.generateId();
        reminder.setId(id);
        reminder.setCode(dataStore.generateCode("REM"));
        reminder.setType(Reminder.TYPE_BOARDING_OVERDUE);
        reminder.setOwnerId(boarding.getOwnerId());
        reminder.setOwnerName(owner != null ? owner.getName() : "");
        reminder.setOwnerPhone(owner != null ? owner.getPhone() : "");
        reminder.setPetId(boarding.getPetId());
        reminder.setPetName(pet != null ? pet.getName() : "");
        reminder.setRelatedId(boarding.getId());
        reminder.setRelatedCode(boarding.getCode());
        reminder.setRemindDate(LocalDate.now());
        reminder.setContent("您的宠物【" + (pet != null ? pet.getName() : "") + "】寄养已超期，请尽快接走，超期将继续计费。当前超期费用：" + boarding.getExtraDaysAmount() + "元");
        reminder.setStatus(Reminder.STATUS_PENDING);

        dataStore.getReminders().put(id, reminder);

        return reminder;
    }

    public Reminder markAsSent(Long id) {
        Reminder reminder = dataStore.getReminders().get(id);
        if (reminder == null) {
            return null;
        }
        reminder.setStatus(Reminder.STATUS_SENT);
        return reminder;
    }

    public Reminder cancelReminder(Long id) {
        Reminder reminder = dataStore.getReminders().get(id);
        if (reminder == null) {
            return null;
        }
        reminder.setStatus(Reminder.STATUS_CANCELLED);
        return reminder;
    }
}
