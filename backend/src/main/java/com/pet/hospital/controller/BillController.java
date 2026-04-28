package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.Bill;
import com.pet.hospital.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "*")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public Result<List<Bill>> getAllBills() {
        return Result.success(billService.getAllBills());
    }

    @GetMapping("/{id}")
    public Result<Bill> getBillById(@PathVariable Long id) {
        Bill bill = billService.getBillById(id);
        if (bill == null) {
            return Result.error("账单不存在");
        }
        return Result.success(bill);
    }

    @PostMapping("/{id}/pay")
    public Result<Bill> payBill(
            @PathVariable Long id,
            @RequestParam(defaultValue = "CASH") String paymentMethod,
            @RequestBody(required = false) Map<String, Object> extraParams) {
        try {
            Map<Long, Map<Long, Integer>> dispensedMedicines = null;
            if (extraParams != null && extraParams.containsKey("dispensedMedicines")) {
                dispensedMedicines = (Map<Long, Map<Long, Integer>>) extraParams.get("dispensedMedicines");
            }
            Bill paid = billService.payBill(id, paymentMethod, dispensedMedicines);
            return Result.success(paid);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/refund")
    public Result<Boolean> refundBill(@PathVariable Long id) {
        try {
            billService.refundBill(id);
            return Result.success(true);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
