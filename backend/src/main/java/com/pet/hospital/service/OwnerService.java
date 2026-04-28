package com.pet.hospital.service;

import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OwnerService {

    @Autowired
    private DataStore dataStore;

    public List<Owner> getAllOwners() {
        return new ArrayList<>(dataStore.getOwners().values());
    }

    public Owner getOwnerById(Long id) {
        return dataStore.getOwners().get(id);
    }

    public Owner getOwnerByPhone(String phone) {
        return dataStore.getOwners().values().stream()
                .filter(o -> o.getPhone().equals(phone))
                .findFirst()
                .orElse(null);
    }

    public Owner createOwner(Owner owner) {
        Long id = dataStore.generateId();
        owner.setId(id);
        owner.setMemberLevel(MemberLevel.NORMAL);
        owner.setPoints(0);
        dataStore.getOwners().put(id, owner);
        return owner;
    }

    public Owner updateOwner(Long id, Owner owner) {
        if (!dataStore.getOwners().containsKey(id)) {
            return null;
        }
        owner.setId(id);
        dataStore.getOwners().put(id, owner);
        return owner;
    }

    public boolean deleteOwner(Long id) {
        return dataStore.getOwners().remove(id) != null;
    }

    public BigDecimal getTreatmentDiscount(Long ownerId) {
        Owner owner = dataStore.getOwners().get(ownerId);
        if (owner == null) {
            return BigDecimal.ONE;
        }
        return owner.getMemberLevel().getTreatmentDiscount();
    }

    public BigDecimal getMedicineDiscount(Long ownerId) {
        Owner owner = dataStore.getOwners().get(ownerId);
        if (owner == null) {
            return BigDecimal.ONE;
        }
        return owner.getMemberLevel().getMedicineDiscount();
    }

    public void addPoints(Long ownerId, int points) {
        Owner owner = dataStore.getOwners().get(ownerId);
        if (owner != null) {
            int newPoints = owner.getPoints() + points;
            owner.setPoints(newPoints);
            updateMemberLevel(owner);
        }
    }

    public void deductPoints(Long ownerId, int points) {
        Owner owner = dataStore.getOwners().get(ownerId);
        if (owner != null && owner.getPoints() >= points) {
            owner.setPoints(owner.getPoints() - points);
            updateMemberLevel(owner);
        }
    }

    private void updateMemberLevel(Owner owner) {
        int points = owner.getPoints();
        if (points >= 5000) {
            owner.setMemberLevel(MemberLevel.PLATINUM);
        } else if (points >= 2000) {
            owner.setMemberLevel(MemberLevel.GOLD);
        } else if (points >= 500) {
            owner.setMemberLevel(MemberLevel.SILVER);
        } else {
            owner.setMemberLevel(MemberLevel.NORMAL);
        }
    }

    public void rollbackPoints(Long ownerId, int points) {
        Owner owner = dataStore.getOwners().get(ownerId);
        if (owner != null) {
            owner.setPoints(Math.max(0, owner.getPoints() - points));
            updateMemberLevel(owner);
        }
    }
}
