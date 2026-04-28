package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.Pet;
import com.pet.hospital.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping("/test")
    public Result<Map<String, Object>> test() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "OK");
        result.put("message", "PetController 测试成功！");
        return Result.success(result);
    }

    @GetMapping
    public Result<List<Pet>> getAllPets() {
        try {
            List<Pet> pets = petService.getAllPets();
            return Result.success(pets);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取宠物列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Pet> getPetById(@PathVariable Long id) {
        Pet pet = petService.getPetById(id);
        if (pet == null) {
            return Result.error("宠物不存在");
        }
        return Result.success(pet);
    }

    @GetMapping("/owner/{ownerId}")
    public Result<List<Pet>> getPetsByOwnerId(@PathVariable Long ownerId) {
        return Result.success(petService.getPetsByOwnerId(ownerId));
    }

    @PostMapping
    public Result<Pet> createPet(@RequestBody Pet pet) {
        return Result.success(petService.createPet(pet));
    }

    @PutMapping("/{id}")
    public Result<Pet> updatePet(@PathVariable Long id, @RequestBody Pet pet) {
        Pet updated = petService.updatePet(id, pet);
        if (updated == null) {
            return Result.error("宠物不存在");
        }
        return Result.success(updated);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deletePet(@PathVariable Long id) {
        boolean deleted = petService.deletePet(id);
        if (!deleted) {
            return Result.error("宠物不存在");
        }
        return Result.success(true);
    }
}
