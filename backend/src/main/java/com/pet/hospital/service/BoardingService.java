package com.pet.hospital.service;

import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardingService {

    @Autowired
    private DataStore dataStore;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ReminderService reminderService;

    public List<Boarding> getAllBoardings() {
        return new ArrayList<>(dataStore.getBoardings().values());
    }

    public Boarding getBoardingById(Long id) {
        return dataStore.getBoardings().get(id);
    }

    public List<Boarding> getBoardingsByOwnerId(Long ownerId) {
        return dataStore.getBoardings().values().stream()
                .filter(b -> b.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public List<Cage> getAvailableCages(String type, BigDecimal petWeight) {
        return dataStore.getCages().values().stream()
                .filter(c -> "AVAILABLE".equals(c.getStatus()))
                .filter(c -> type == null || type.isEmpty() || c.getType().equals(type))
                .filter(c -> petWeight == null || petWeight.compareTo(new BigDecimal(c.getMaxWeight())) <= 0)
                .collect(Collectors.toList());
    }

    public Boarding createBoarding(Boarding boarding) {
        Cage cage = dataStore.getCages().get(boarding.getCageId());
        if (cage == null || !"AVAILABLE".equals(cage.getStatus())) {
            throw new RuntimeException("笼位不可用");
        }

        if (boarding.getPetWeight() != null
                && boarding.getPetWeight().compareTo(new BigDecimal(cage.getMaxWeight())) > 0) {
            throw new RuntimeException("宠物体重超过笼位承重限制");
        }

        Owner owner = dataStore.getOwners().get(boarding.getOwnerId());
        if (owner != null) {
            boarding.setOwnerName(owner.getName());
            boarding.setOwnerPhone(owner.getPhone());
        }

        Pet pet = dataStore.getPets().get(boarding.getPetId());
        if (pet != null) {
            boarding.setPetName(pet.getName());
            if (boarding.getPetWeight() == null) {
                boarding.setPetWeight(pet.getWeight());
            }
        }

        cage.setStatus("OCCUPIED");
        boarding.setCageName(cage.getName());
        boarding.setDailyRate(cage.getDailyRate());

        int days = (int) ChronoUnit.DAYS.between(boarding.getCheckInDate(), boarding.getExpectedCheckOutDate()) + 1;
        boarding.setDays(days);
        boarding.setTotalAmount(cage.getDailyRate().multiply(new BigDecimal(days)));
        boarding.setTotalPayable(boarding.getTotalAmount());
        boarding.setExtraDaysAmount(BigDecimal.ZERO);

        Long id = dataStore.generateId();
        boarding.setId(id);
        boarding.setCode(dataStore.generateCode("BORD"));
        boarding.setStatus("CHECKED_IN");

        dataStore.getBoardings().put(id, boarding);

        return boarding;
    }

    public Boarding checkOut(Long boardingId, LocalDate actualCheckOutDate) {
        Boarding boarding = dataStore.getBoardings().get(boardingId);
        if (boarding == null || !"CHECKED_IN".equals(boarding.getStatus())) {
            throw new RuntimeException("寄养状态不正确");
        }

        if (actualCheckOutDate == null) {
            actualCheckOutDate = LocalDate.now();
        }

        if (actualCheckOutDate.isBefore(boarding.getCheckInDate())) {
            throw new RuntimeException("实际退房日期不能早于入住日期");
        }

        int actualDays = (int) ChronoUnit.DAYS.between(boarding.getCheckInDate(), actualCheckOutDate) + 1;
        BigDecimal actualTotal = boarding.getDailyRate().multiply(new BigDecimal(actualDays));

        BigDecimal extraAmount = BigDecimal.ZERO;
        if (actualCheckOutDate.isAfter(boarding.getExpectedCheckOutDate())) {
            int extraDays = (int) ChronoUnit.DAYS.between(boarding.getExpectedCheckOutDate(), actualCheckOutDate);
            extraAmount = boarding.getDailyRate().multiply(new BigDecimal(extraDays));
            boarding.setExtraDaysAmount(extraAmount);
        }

        boarding.setActualCheckOutDate(actualCheckOutDate);
        boarding.setDays(actualDays);
        boarding.setTotalAmount(actualTotal);
        boarding.setTotalPayable(actualTotal);
        boarding.setStatus("COMPLETED");

        Cage cage = dataStore.getCages().get(boarding.getCageId());
        if (cage != null) {
            cage.setStatus("AVAILABLE");
        }

        return boarding;
    }

    public Boarding updateBoarding(Long id, Boarding boarding) {
        if (!dataStore.getBoardings().containsKey(id)) {
            return null;
        }
        boarding.setId(id);
        dataStore.getBoardings().put(id, boarding);
        return boarding;
    }

    public List<Boarding> getOverdueBoardings() {
        LocalDate today = LocalDate.now();
        return dataStore.getBoardings().values().stream()
                .filter(b -> "CHECKED_IN".equals(b.getStatus()))
                .filter(b -> b.getExpectedCheckOutDate().isBefore(today))
                .collect(Collectors.toList());
    }

    public void updateOverdueBoardingFees() {
        List<Boarding> overdueBoardings = getOverdueBoardings();
        for (Boarding boarding : overdueBoardings) {
            LocalDate today = LocalDate.now();
            int totalOverdueDays = (int) ChronoUnit.DAYS.between(boarding.getExpectedCheckOutDate(), today);
            BigDecimal overdueAmount = boarding.getDailyRate().multiply(new BigDecimal(totalOverdueDays));
            boarding.setExtraDaysAmount(overdueAmount);
            boarding.setTotalPayable(boarding.getTotalAmount().add(overdueAmount));

            reminderService.createOverdueReminder(boarding);
        }
    }
}
