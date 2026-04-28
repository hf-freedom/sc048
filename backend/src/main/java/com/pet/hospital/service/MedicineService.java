package com.pet.hospital.service;

import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    @Autowired
    private DataStore dataStore;

    public List<Medicine> getAllMedicines() {
        return new ArrayList<>(dataStore.getMedicines().values());
    }

    public Medicine getMedicineById(Long id) {
        return dataStore.getMedicines().get(id);
    }

    public Medicine createMedicine(Medicine medicine) {
        Long id = dataStore.generateId();
        medicine.setId(id);
        dataStore.getMedicines().put(id, medicine);
        return medicine;
    }

    public Medicine updateMedicine(Long id, Medicine medicine) {
        if (!dataStore.getMedicines().containsKey(id)) {
            return null;
        }
        medicine.setId(id);
        dataStore.getMedicines().put(id, medicine);
        return medicine;
    }

    public boolean deleteMedicine(Long id) {
        return dataStore.getMedicines().remove(id) != null;
    }

    public int getTotalStock(Long medicineId) {
        return dataStore.getMedicineBatches().values().stream()
                .filter(b -> b.getMedicineId().equals(medicineId) && "ACTIVE".equals(b.getStatus()))
                .mapToInt(MedicineBatch::getAvailableQuantity)
                .sum();
    }

    public List<MedicineBatch> getMedicineBatches(Long medicineId) {
        return dataStore.getMedicineBatches().values().stream()
                .filter(b -> b.getMedicineId().equals(medicineId))
                .sorted(Comparator.comparing(MedicineBatch::getExpiryDate))
                .collect(Collectors.toList());
    }

    public List<MedicineBatch> getAvailableBatchesForDispensing(Long medicineId) {
        LocalDate today = LocalDate.now();
        return dataStore.getMedicineBatches().values().stream()
                .filter(b -> b.getMedicineId().equals(medicineId)
                        && "ACTIVE".equals(b.getStatus())
                        && b.getAvailableQuantity() > 0
                        && !b.getExpiryDate().isBefore(today))
                .sorted(Comparator.comparing(MedicineBatch::getExpiryDate))
                .collect(Collectors.toList());
    }

    public MedicineBatch createMedicineBatch(MedicineBatch batch) {
        Long id = dataStore.generateId();
        batch.setId(id);
        batch.setAvailableQuantity(batch.getQuantity());
        dataStore.getMedicineBatches().put(id, batch);
        return batch;
    }

    public Map<Long, Integer> dispenseMedicine(Long medicineId, int quantity) {
        List<MedicineBatch> batches = getAvailableBatchesForDispensing(medicineId);
        Map<Long, Integer> dispensedBatches = new HashMap<>();
        int remainingQuantity = quantity;

        for (MedicineBatch batch : batches) {
            if (remainingQuantity <= 0) {
                break;
            }
            int toDispense = Math.min(batch.getAvailableQuantity(), remainingQuantity);
            batch.setAvailableQuantity(batch.getAvailableQuantity() - toDispense);
            dispensedBatches.put(batch.getId(), toDispense);
            remainingQuantity -= toDispense;
        }

        if (remainingQuantity > 0) {
            throw new RuntimeException("药品库存不足，药品ID: " + medicineId);
        }

        return dispensedBatches;
    }

    public void rollbackDispense(Long medicineId, Map<Long, Integer> batchQuantities) {
        for (Map.Entry<Long, Integer> entry : batchQuantities.entrySet()) {
            MedicineBatch batch = dataStore.getMedicineBatches().get(entry.getKey());
            if (batch != null && batch.getMedicineId().equals(medicineId)) {
                batch.setAvailableQuantity(batch.getAvailableQuantity() + entry.getValue());
            }
        }
    }

    public List<MedicineBatch> getExpiringMedicines(int daysThreshold) {
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        return dataStore.getMedicineBatches().values().stream()
                .filter(b -> "ACTIVE".equals(b.getStatus())
                        && b.getAvailableQuantity() > 0
                        && !b.getExpiryDate().isAfter(thresholdDate))
                .sorted(Comparator.comparing(MedicineBatch::getExpiryDate))
                .collect(Collectors.toList());
    }
}
