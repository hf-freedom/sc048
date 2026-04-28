package com.pet.hospital.service;

import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConsumableService {

    @Autowired
    private DataStore dataStore;

    public List<Consumable> getAllConsumables() {
        return new ArrayList<>(dataStore.getConsumables().values());
    }

    public Consumable getConsumableById(Long id) {
        return dataStore.getConsumables().get(id);
    }

    public Consumable createConsumable(Consumable consumable) {
        Long id = dataStore.generateId();
        consumable.setId(id);
        dataStore.getConsumables().put(id, consumable);
        return consumable;
    }

    public Consumable updateConsumable(Long id, Consumable consumable) {
        if (!dataStore.getConsumables().containsKey(id)) {
            return null;
        }
        consumable.setId(id);
        dataStore.getConsumables().put(id, consumable);
        return consumable;
    }

    public boolean deleteConsumable(Long id) {
        return dataStore.getConsumables().remove(id) != null;
    }

    public boolean consumeConsumable(Long id, int quantity) {
        Consumable consumable = dataStore.getConsumables().get(id);
        if (consumable == null || consumable.getStock() < quantity) {
            return false;
        }
        consumable.setStock(consumable.getStock() - quantity);
        return true;
    }

    public void rollbackConsumable(Long id, int quantity) {
        Consumable consumable = dataStore.getConsumables().get(id);
        if (consumable != null) {
            consumable.setStock(consumable.getStock() + quantity);
        }
    }

    public void addStock(Long id, int quantity) {
        Consumable consumable = dataStore.getConsumables().get(id);
        if (consumable != null) {
            consumable.setStock(consumable.getStock() + quantity);
        }
    }
}
