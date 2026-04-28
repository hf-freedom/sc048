package com.pet.hospital.task;

import com.pet.hospital.model.Reminder;
import com.pet.hospital.service.BoardingService;
import com.pet.hospital.service.ReminderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private BoardingService boardingService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void processPendingReminders() {
        logger.info("开始执行定时任务：处理待发送的提醒");
        
        List<Reminder> pendingReminders = reminderService.getPendingReminders();
        logger.info("找到 {} 个待发送的提醒", pendingReminders.size());

        for (Reminder reminder : pendingReminders) {
            logger.info("发送提醒：类型={}, 宠物={}, 内容={}", 
                    reminder.getType(), reminder.getPetName(), reminder.getContent());
            
            reminderService.markAsSent(reminder.getId());
        }

        logger.info("定时任务执行完成：处理待发送的提醒");
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void checkOverdueBoardings() {
        logger.info("开始执行定时任务：检查寄养超期");
        
        boardingService.updateOverdueBoardingFees();
        
        logger.info("定时任务执行完成：检查寄养超期");
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void generateDailyReports() {
        logger.info("开始执行定时任务：生成每日报告");
        logger.info("定时任务执行完成：生成每日报告");
    }
}
