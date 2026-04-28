package com.pet.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PetHospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetHospitalApplication.class, args);
        System.out.println("==========================================");
        System.out.println("  宠物医院管理系统启动成功");
        System.out.println("  访问地址: http://localhost:8001");
        System.out.println("==========================================");
    }
}
