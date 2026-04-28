package com.pet.hospital.service;

import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    @Autowired
    private DataStore dataStore;

    @Autowired
    private PetService petService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private OwnerService ownerService;

    public List<Prescription> getAllPrescriptions() {
        return new ArrayList<>(dataStore.getPrescriptions().values());
    }

    public Prescription getPrescriptionById(Long id) {
        return dataStore.getPrescriptions().get(id);
    }

    public List<Prescription> getPrescriptionsByOwnerId(Long ownerId) {
        return dataStore.getPrescriptions().values().stream()
                .filter(p -> p.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public Prescription createPrescription(Prescription prescription) {
        Pet pet = dataStore.getPets().get(prescription.getPetId());
        if (pet == null) {
            throw new RuntimeException("宠物不存在");
        }

        for (PrescriptionItem item : prescription.getItems()) {
            Medicine medicine = dataStore.getMedicines().get(item.getMedicineId());
            if (medicine == null) {
                throw new RuntimeException("药品不存在，ID: " + item.getMedicineId());
            }

            if (petService.isAllergicToMedicine(pet.getId(), medicine.getName())) {
                throw new RuntimeException("宠物对该药品过敏: " + medicine.getName());
            }

            if (medicineService.getTotalStock(medicine.getId()) < item.getQuantity()) {
                throw new RuntimeException("药品库存不足: " + medicine.getName());
            }

            item.setMedicineName(medicine.getName());
            item.setSpecification(medicine.getSpecification());
            item.setUnit(medicine.getUnit());
            item.setUnitPrice(medicine.getSalePrice());
            item.setAmount(medicine.getSalePrice().multiply(new BigDecimal(item.getQuantity())));
        }

        BigDecimal totalAmount = prescription.getItems().stream()
                .map(PrescriptionItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = ownerService.getMedicineDiscount(prescription.getOwnerId());
        BigDecimal discountAmount = totalAmount.multiply(BigDecimal.ONE.subtract(discount))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal payableAmount = totalAmount.multiply(discount).setScale(2, RoundingMode.HALF_UP);

        Long id = dataStore.generateId();
        prescription.setId(id);
        prescription.setCode(dataStore.generateCode("PRES"));
        prescription.setTotalAmount(totalAmount);
        prescription.setDiscountAmount(discountAmount);
        prescription.setPayableAmount(payableAmount);
        prescription.setStatus("PENDING");

        int itemIndex = 1;
        for (PrescriptionItem item : prescription.getItems()) {
            item.setId(dataStore.generateId());
            item.setPrescriptionId(id);
            itemIndex++;
        }

        dataStore.getPrescriptions().put(id, prescription);

        return prescription;
    }

    public Prescription updatePrescription(Long id, Prescription prescription) {
        if (!dataStore.getPrescriptions().containsKey(id)) {
            return null;
        }
        prescription.setId(id);
        dataStore.getPrescriptions().put(id, prescription);
        return prescription;
    }

    public Map<Long, Map<Long, Integer>> dispensePrescription(Long prescriptionId) {
        Prescription prescription = dataStore.getPrescriptions().get(prescriptionId);
        if (prescription == null || !"PENDING".equals(prescription.getStatus())) {
            throw new RuntimeException("处方状态不正确");
        }

        Map<Long, Map<Long, Integer>> dispensedMedicines = new HashMap<>();

        for (PrescriptionItem item : prescription.getItems()) {
            Map<Long, Integer> batches = medicineService.dispenseMedicine(item.getMedicineId(), item.getQuantity());
            dispensedMedicines.put(item.getMedicineId(), batches);
        }

        prescription.setStatus("DISPENSED");

        return dispensedMedicines;
    }

    public void rollbackDispense(Long prescriptionId, Map<Long, Map<Long, Integer>> dispensedMedicines) {
        Prescription prescription = dataStore.getPrescriptions().get(prescriptionId);
        if (prescription == null) {
            return;
        }

        for (Map.Entry<Long, Map<Long, Integer>> entry : dispensedMedicines.entrySet()) {
            medicineService.rollbackDispense(entry.getKey(), entry.getValue());
        }

        prescription.setStatus("PENDING");
    }

    public Prescription cancelPrescription(Long id) {
        Prescription prescription = dataStore.getPrescriptions().get(id);
        if (prescription == null) {
            return null;
        }
        prescription.setStatus("CANCELLED");
        return prescription;
    }
}
