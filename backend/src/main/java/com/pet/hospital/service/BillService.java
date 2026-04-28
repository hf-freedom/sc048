package com.pet.hospital.service;

import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BillService {

    @Autowired
    private DataStore dataStore;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private ConsumableService consumableService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private BoardingService boardingService;

    public List<Bill> getAllBills() {
        return new ArrayList<>(dataStore.getBills().values());
    }

    public Bill getBillById(Long id) {
        return dataStore.getBills().get(id);
    }

    public Bill createConsultationBill(Consultation consultation, Prescription prescription) {
        Owner owner = dataStore.getOwners().get(consultation.getOwnerId());
        if (owner == null) {
            throw new RuntimeException("主人不存在");
        }

        Bill bill = new Bill();
        Long id = dataStore.generateId();
        bill.setId(id);
        bill.setCode(dataStore.generateCode("BILL"));
        bill.setType("CONSULTATION");
        bill.setOwnerId(consultation.getOwnerId());
        bill.setOwnerName(owner.getName());
        bill.setRelatedId(consultation.getId());
        bill.setRelatedCode(consultation.getCode());

        List<BillItem> items = new ArrayList<>();
        BigDecimal treatmentDiscount = ownerService.getTreatmentDiscount(owner.getId());
        BigDecimal medicineDiscount = ownerService.getMedicineDiscount(owner.getId());
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Long treatmentId : consultation.getTreatmentItemIds()) {
            TreatmentItem item = dataStore.getTreatmentItems().get(treatmentId);
            if (item != null) {
                BillItem billItem = new BillItem();
                billItem.setId(dataStore.generateId());
                billItem.setBillId(id);
                billItem.setItemType("TREATMENT");
                billItem.setItemId(item.getId());
                billItem.setItemName(item.getName());
                billItem.setQuantity(1);
                billItem.setUnitPrice(item.getPrice());
                billItem.setAmount(item.getPrice());
                items.add(billItem);
                totalAmount = totalAmount.add(item.getPrice());
            }
        }

        if (prescription != null && !prescription.getItems().isEmpty()) {
            for (PrescriptionItem presItem : prescription.getItems()) {
                BillItem billItem = new BillItem();
                billItem.setId(dataStore.generateId());
                billItem.setBillId(id);
                billItem.setItemType("MEDICINE");
                billItem.setItemId(presItem.getMedicineId());
                billItem.setItemName(presItem.getMedicineName());
                billItem.setQuantity(presItem.getQuantity());
                billItem.setUnitPrice(presItem.getUnitPrice());
                billItem.setAmount(presItem.getAmount());
                items.add(billItem);
            }
            totalAmount = totalAmount.add(prescription.getTotalAmount());
        }

        BigDecimal discountAmount = totalAmount.multiply(BigDecimal.ONE.subtract(treatmentDiscount))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal payableAmount = totalAmount.multiply(treatmentDiscount).setScale(2, RoundingMode.HALF_UP);

        bill.setItems(items);
        bill.setTotalAmount(totalAmount);
        bill.setDiscountAmount(discountAmount);
        bill.setPayableAmount(payableAmount);
        bill.setPaidAmount(BigDecimal.ZERO);

        dataStore.getBills().put(id, bill);

        return bill;
    }

    public Bill createBoardingBill(Boarding boarding) {
        Owner owner = dataStore.getOwners().get(boarding.getOwnerId());
        if (owner == null) {
            throw new RuntimeException("主人不存在");
        }

        Bill bill = new Bill();
        Long id = dataStore.generateId();
        bill.setId(id);
        bill.setCode(dataStore.generateCode("BILL"));
        bill.setType("BOARDING");
        bill.setOwnerId(boarding.getOwnerId());
        bill.setOwnerName(owner.getName());
        bill.setRelatedId(boarding.getId());
        bill.setRelatedCode(boarding.getCode());

        List<BillItem> items = new ArrayList<>();
        BigDecimal totalAmount = boarding.getTotalPayable();

        BillItem billItem = new BillItem();
        billItem.setId(dataStore.generateId());
        billItem.setBillId(id);
        billItem.setItemType("BOARDING");
        billItem.setItemId(boarding.getCageId());
        billItem.setItemName("寄养费用: " + boarding.getCageName());
        billItem.setQuantity(boarding.getDays());
        billItem.setUnitPrice(boarding.getDailyRate());
        billItem.setAmount(totalAmount);
        items.add(billItem);

        if (boarding.getExtraDaysAmount() != null && boarding.getExtraDaysAmount().compareTo(BigDecimal.ZERO) > 0) {
            BillItem extraItem = new BillItem();
            extraItem.setId(dataStore.generateId());
            extraItem.setBillId(id);
            extraItem.setItemType("BOARDING_EXTRA");
            extraItem.setItemId(boarding.getCageId());
            extraItem.setItemName("超期寄养费用");
            int extraDays = boarding.getDays() -
                    (int) java.time.temporal.ChronoUnit.DAYS.between(
                            boarding.getCheckInDate(), boarding.getExpectedCheckOutDate()) - 1;
            extraItem.setQuantity(Math.max(extraDays, 0));
            extraItem.setUnitPrice(boarding.getDailyRate());
            extraItem.setAmount(boarding.getExtraDaysAmount());
            items.add(extraItem);
        }

        bill.setItems(items);
        bill.setTotalAmount(totalAmount);
        bill.setDiscountAmount(BigDecimal.ZERO);
        bill.setPayableAmount(totalAmount);
        bill.setPaidAmount(BigDecimal.ZERO);

        dataStore.getBills().put(id, bill);

        return bill;
    }

    public Bill payBill(Long billId, String paymentMethod, Map<Long, Map<Long, Integer>> dispensedMedicines) {
        Bill bill = dataStore.getBills().get(billId);
        if (bill == null || !"PENDING".equals(bill.getStatus())) {
            throw new RuntimeException("账单状态不正确");
        }

        bill.setPaymentMethod(paymentMethod);
        bill.setPaidAmount(bill.getPayableAmount());
        bill.setStatus("PAID");
        bill.setPaidTime(LocalDateTime.now());

        int pointsEarned = bill.getPaidAmount().intValue();
        ownerService.addPoints(bill.getOwnerId(), pointsEarned);

        if ("CONSULTATION".equals(bill.getType())) {
            if (dispensedMedicines != null && !dispensedMedicines.isEmpty()) {
                for (Map.Entry<Long, Map<Long, Integer>> entry : dispensedMedicines.entrySet()) {
                    medicineService.dispenseMedicine(entry.getKey(),
                            entry.getValue().values().stream().mapToInt(Integer::intValue).sum());
                }
            }
        }

        return bill;
    }

    public void refundBill(Long billId) {
        Bill bill = dataStore.getBills().get(billId);
        if (bill == null || !"PAID".equals(bill.getStatus())) {
            throw new RuntimeException("账单状态不正确，无法退款");
        }

        bill.setStatus("REFUNDED");

        int pointsToDeduct = bill.getPaidAmount().intValue();
        ownerService.rollbackPoints(bill.getOwnerId(), pointsToDeduct);

        if ("CONSULTATION".equals(bill.getType()) && bill.getRelatedId() != null) {
            Consultation consultation = dataStore.getConsultations().get(bill.getRelatedId());
            if (consultation != null && consultation.getPrescriptionId() != null) {
                Prescription prescription = dataStore.getPrescriptions().get(consultation.getPrescriptionId());
                if (prescription != null && "DISPENSED".equals(prescription.getStatus())) {
                    prescription.setStatus("REFUNDED");
                }
            }
        }
    }
}
