package com.pet.hospital.controller;

import com.pet.hospital.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @GetMapping("/health")
    public Result<Map<String, Object>> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "OK");
        result.put("timestamp", System.currentTimeMillis());
        result.put("localDate", LocalDate.now());
        result.put("localTime", LocalTime.now());
        result.put("localDateTime", LocalDateTime.now());
        return Result.success(result);
    }

    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("后端服务正常运行！");
    }
}
