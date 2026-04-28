package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/treatments")
@CrossOrigin(origins = "*")
public class TreatmentController {

    @Autowired
    private DataStore dataStore;

    @GetMapping
    public Result<List<TreatmentItem>> getAllTreatments() {
        return Result.success(new ArrayList<>(dataStore.getTreatmentItems().values()));
    }

    @GetMapping("/{id}")
    public Result<TreatmentItem> getTreatmentById(@PathVariable Long id) {
        TreatmentItem item = dataStore.getTreatmentItems().get(id);
        if (item == null) {
            return Result.error("诊疗项目不存在");
        }
        return Result.success(item);
    }

    @PostMapping
    public Result<TreatmentItem> createTreatment(@RequestBody TreatmentItem item) {
        Long id = dataStore.generateId();
        item.setId(id);
        item.setCode(dataStore.generateCode("T"));
        dataStore.getTreatmentItems().put(id, item);
        return Result.success(item);
    }

    @PutMapping("/{id}")
    public Result<TreatmentItem> updateTreatment(@PathVariable Long id, @RequestBody TreatmentItem item) {
        if (!dataStore.getTreatmentItems().containsKey(id)) {
            return Result.error("诊疗项目不存在");
        }
        item.setId(id);
        dataStore.getTreatmentItems().put(id, item);
        return Result.success(item);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTreatment(@PathVariable Long id) {
        if (!dataStore.getTreatmentItems().containsKey(id)) {
            return Result.error("诊疗项目不存在");
        }
        dataStore.getTreatmentItems().remove(id);
        return Result.success(true);
    }

    @GetMapping("/departments")
    public Result<List<Department>> getAllDepartments() {
        return Result.success(new ArrayList<>(dataStore.getDepartments().values()));
    }

    @GetMapping("/departments/{id}")
    public Result<Department> getDepartmentById(@PathVariable Long id) {
        Department dept = dataStore.getDepartments().get(id);
        if (dept == null) {
            return Result.error("科室不存在");
        }
        return Result.success(dept);
    }

    @GetMapping("/cages")
    public Result<List<Cage>> getAllCages() {
        return Result.success(new ArrayList<>(dataStore.getCages().values()));
    }

    @GetMapping("/cages/available")
    public Result<List<Cage>> getAvailableCages(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) java.math.BigDecimal petWeight) {
        List<Cage> result = new ArrayList<>();
        for (Cage cage : dataStore.getCages().values()) {
            if (!"AVAILABLE".equals(cage.getStatus())) {
                continue;
            }
            if (type != null && !type.isEmpty() && !type.equals(cage.getType())) {
                continue;
            }
            if (petWeight != null && petWeight.compareTo(new java.math.BigDecimal(cage.getMaxWeight())) > 0) {
                continue;
            }
            result.add(cage);
        }
        return Result.success(result);
    }
}
