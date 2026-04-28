package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.Owner;
import com.pet.hospital.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin(origins = "*")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public Result<List<Owner>> getAllOwners() {
        return Result.success(ownerService.getAllOwners());
    }

    @GetMapping("/{id}")
    public Result<Owner> getOwnerById(@PathVariable Long id) {
        Owner owner = ownerService.getOwnerById(id);
        if (owner == null) {
            return Result.error("主人不存在");
        }
        return Result.success(owner);
    }

    @GetMapping("/phone/{phone}")
    public Result<Owner> getOwnerByPhone(@PathVariable String phone) {
        Owner owner = ownerService.getOwnerByPhone(phone);
        if (owner == null) {
            return Result.error("主人不存在");
        }
        return Result.success(owner);
    }

    @PostMapping
    public Result<Owner> createOwner(@RequestBody Owner owner) {
        return Result.success(ownerService.createOwner(owner));
    }

    @PutMapping("/{id}")
    public Result<Owner> updateOwner(@PathVariable Long id, @RequestBody Owner owner) {
        Owner updated = ownerService.updateOwner(id, owner);
        if (updated == null) {
            return Result.error("主人不存在");
        }
        return Result.success(updated);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteOwner(@PathVariable Long id) {
        boolean deleted = ownerService.deleteOwner(id);
        if (!deleted) {
            return Result.error("主人不存在");
        }
        return Result.success(true);
    }

    @PostMapping("/{id}/points/add")
    public Result<Boolean> addPoints(@PathVariable Long id, @RequestParam int points) {
        ownerService.addPoints(id, points);
        return Result.success(true);
    }

    @PostMapping("/{id}/points/deduct")
    public Result<Boolean> deductPoints(@PathVariable Long id, @RequestParam int points) {
        ownerService.deductPoints(id, points);
        return Result.success(true);
    }
}
