package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.*;
import com.pet.hospital.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/boardings")
@CrossOrigin(origins = "*")
public class BoardingController {

    @Autowired
    private BoardingService boardingService;

    @Autowired
    private BillService billService;

    @GetMapping
    public Result<List<Boarding>> getAllBoardings() {
        return Result.success(boardingService.getAllBoardings());
    }

    @GetMapping("/{id}")
    public Result<Boarding> getBoardingById(@PathVariable Long id) {
        Boarding boarding = boardingService.getBoardingById(id);
        if (boarding == null) {
            return Result.error("寄养记录不存在");
        }
        return Result.success(boarding);
    }

    @GetMapping("/owner/{ownerId}")
    public Result<List<Boarding>> getBoardingsByOwnerId(@PathVariable Long ownerId) {
        return Result.success(boardingService.getBoardingsByOwnerId(ownerId));
    }

    @GetMapping("/overdue")
    public Result<List<Boarding>> getOverdueBoardings() {
        return Result.success(boardingService.getOverdueBoardings());
    }

    @PostMapping
    public Result<Boarding> createBoarding(@RequestBody Boarding boarding) {
        try {
            Boarding created = boardingService.createBoarding(boarding);
            return Result.success(created);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Boarding> updateBoarding(@PathVariable Long id, @RequestBody Boarding boarding) {
        Boarding updated = boardingService.updateBoarding(id, boarding);
        if (updated == null) {
            return Result.error("寄养记录不存在");
        }
        return Result.success(updated);
    }

    @PostMapping("/{id}/checkout")
    public Result<Boarding> checkOut(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate actualCheckOutDate) {
        try {
            Boarding checkedOut = boardingService.checkOut(id, actualCheckOutDate);
            return Result.success(checkedOut);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/bill")
    public Result<Bill> createBill(@PathVariable Long id) {
        Boarding boarding = boardingService.getBoardingById(id);
        if (boarding == null) {
            return Result.error("寄养记录不存在");
        }
        Bill bill = billService.createBoardingBill(boarding);
        boarding.setBillId(bill.getId());
        return Result.success(bill);
    }
}
