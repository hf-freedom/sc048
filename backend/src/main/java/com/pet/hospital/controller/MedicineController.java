package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.Medicine;
import com.pet.hospital.model.MedicineBatch;
import com.pet.hospital.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin(origins = "*")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping
    public Result<List<Medicine>> getAllMedicines() {
        return Result.success(medicineService.getAllMedicines());
    }

    @GetMapping("/{id}")
    public Result<Medicine> getMedicineById(@PathVariable Long id) {
        Medicine medicine = medicineService.getMedicineById(id);
        if (medicine == null) {
            return Result.error("药品不存在");
        }
        return Result.success(medicine);
    }

    @GetMapping("/{id}/stock")
    public Result<Integer> getTotalStock(@PathVariable Long id) {
        return Result.success(medicineService.getTotalStock(id));
    }

    @GetMapping("/{id}/batches")
    public Result<List<MedicineBatch>> getMedicineBatches(@PathVariable Long id) {
        return Result.success(medicineService.getMedicineBatches(id));
    }

    @GetMapping("/{id}/batches/available")
    public Result<List<MedicineBatch>> getAvailableBatches(@PathVariable Long id) {
        return Result.success(medicineService.getAvailableBatchesForDispensing(id));
    }

    @PostMapping
    public Result<Medicine> createMedicine(@RequestBody Medicine medicine) {
        return Result.success(medicineService.createMedicine(medicine));
    }

    @PutMapping("/{id}")
    public Result<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
        Medicine updated = medicineService.updateMedicine(id, medicine);
        if (updated == null) {
            return Result.error("药品不存在");
        }
        return Result.success(updated);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteMedicine(@PathVariable Long id) {
        boolean deleted = medicineService.deleteMedicine(id);
        if (!deleted) {
            return Result.error("药品不存在");
        }
        return Result.success(true);
    }

    @PostMapping("/batches")
    public Result<MedicineBatch> createMedicineBatch(@RequestBody MedicineBatch batch) {
        return Result.success(medicineService.createMedicineBatch(batch));
    }

    @GetMapping("/batches/expiring")
    public Result<List<MedicineBatch>> getExpiringMedicines(@RequestParam(defaultValue = "30") int daysThreshold) {
        return Result.success(medicineService.getExpiringMedicines(daysThreshold));
    }
}
