package com.pet.hospital.service;

import com.pet.hospital.model.*;
import com.pet.hospital.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private DataStore dataStore;

    public List<Pet> getAllPets() {
        return new ArrayList<>(dataStore.getPets().values());
    }

    public Pet getPetById(Long id) {
        return dataStore.getPets().get(id);
    }

    public List<Pet> getPetsByOwnerId(Long ownerId) {
        return dataStore.getPets().values().stream()
                .filter(p -> p.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public Pet createPet(Pet pet) {
        Long id = dataStore.generateId();
        pet.setId(id);
        dataStore.getPets().put(id, pet);
        return pet;
    }

    public Pet updatePet(Long id, Pet pet) {
        if (!dataStore.getPets().containsKey(id)) {
            return null;
        }
        pet.setId(id);
        dataStore.getPets().put(id, pet);
        return pet;
    }

    public boolean deletePet(Long id) {
        return dataStore.getPets().remove(id) != null;
    }

    public boolean isAllergicToMedicine(Long petId, String medicineName) {
        Pet pet = dataStore.getPets().get(petId);
        if (pet == null) {
            return false;
        }
        return pet.getAllergicMedicines().contains(medicineName);
    }

    public void addAllergicMedicine(Long petId, String medicineName) {
        Pet pet = dataStore.getPets().get(petId);
        if (pet != null && !pet.getAllergicMedicines().contains(medicineName)) {
            pet.getAllergicMedicines().add(medicineName);
        }
    }

    public void removeAllergicMedicine(Long petId, String medicineName) {
        Pet pet = dataStore.getPets().get(petId);
        if (pet != null) {
            pet.getAllergicMedicines().remove(medicineName);
        }
    }
}
