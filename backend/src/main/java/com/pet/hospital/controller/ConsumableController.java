package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.Consumable;
import com.pet.hospital.service.ConsumableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumables")
@CrossOrigin(origins = "*")
public class ConsumableController {

    @Autowired
    private ConsumableService consumableService;

    @GetMapping
    public Result<List<Consumable>> getAllConsumables() {
        return Result.success(consumableService.getAllConsumables());
    }

    @GetMapping("/{id}")
    public Result<Consumable> getConsumableById(@PathVariable Long id) {
        Consumable consumable = consumableService.getConsumableById(id);
        if (consumable == null) {
            return Result.error("耗材不存在");
        }
        return Result.success(consumable);
    }

    @PostMapping
    public Result<Consumable> createConsumable(@RequestBody Consumable consumable) {
        return Result.success(consumableService.createConsumable(consumable));
    }

    @PutMapping("/{id}")
    public Result<Consumable> updateConsumable(@PathVariable Long id, @RequestBody Consumable consumable) {
        Consumable updated = consumableService.updateConsumable(id, consumable);
        if (updated == null) {
            return Result.error("耗材不存在");
        }
        return Result.success(updated);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteConsumable(@PathVariable Long id) {
        boolean deleted = consumableService.deleteConsumable(id);
        if (!deleted) {
            return Result.error("耗材不存在");
        }
        return Result.success(true);
    }

    @PostMapping("/{id}/stock/add")
    public Result<Boolean> addStock(@PathVariable Long id, @RequestParam int quantity) {
        consumableService.addStock(id, quantity);
        return Result.success(true);
    }
}
