package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import com.pet.hospital.model.Reminder;
import com.pet.hospital.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin(origins = "*")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @GetMapping
    public Result<List<Reminder>> getAllReminders() {
        return Result.success(reminderService.getAllReminders());
    }

    @GetMapping("/{id}")
    public Result<Reminder> getReminderById(@PathVariable Long id) {
        Reminder reminder = reminderService.getReminderById(id);
        if (reminder == null) {
            return Result.error("提醒不存在");
        }
        return Result.success(reminder);
    }

    @GetMapping("/pending")
    public Result<List<Reminder>> getPendingReminders() {
        return Result.success(reminderService.getPendingReminders());
    }

    @PostMapping("/{id}/send")
    public Result<Reminder> markAsSent(@PathVariable Long id) {
        Reminder sent = reminderService.markAsSent(id);
        if (sent == null) {
            return Result.error("提醒不存在");
        }
        return Result.success(sent);
    }

    @PostMapping("/{id}/cancel")
    public Result<Reminder> cancelReminder(@PathVariable Long id) {
        Reminder cancelled = reminderService.cancelReminder(id);
        if (cancelled == null) {
            return Result.error("提醒不存在");
        }
        return Result.success(cancelled);
    }
}
