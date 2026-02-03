package com.api.feros.service;

import com.api.feros.entity.VehicleMake;
import com.api.feros.repository.VehicleMakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleMakeService {

    private final VehicleMakeRepository vehicleMakeRepository;

    public Page<VehicleMake> getAllVehicleMakesPaged(boolean activeOnly, Pageable pageable) {
        return activeOnly ? vehicleMakeRepository.findByIsActiveTrueOrderByDisplayOrderAsc(pageable) : vehicleMakeRepository.findAllByOrderByDisplayOrderAsc(pageable);
    }

    public List<VehicleMake> getAllVehicleMakes() {
        return vehicleMakeRepository.findAll();
    }

    public List<VehicleMake> getAllActiveVehicleMakes() {
        return vehicleMakeRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    public Optional<VehicleMake> getVehicleMakeById(String id) {
        return vehicleMakeRepository.findById(id);
    }

    @Transactional
    public VehicleMake createVehicleMake(VehicleMake vehicleMake) {
        if (vehicleMakeRepository.existsByNameIgnoreCase(vehicleMake.getName())) {
            throw new IllegalArgumentException("Vehicle make with name '" + vehicleMake.getName() + "' already exists");
        }
        return vehicleMakeRepository.save(vehicleMake);
    }

    @Transactional
    public VehicleMake updateVehicleMake(String id, VehicleMake vehicleMakeDetails) {
        VehicleMake vehicleMake = vehicleMakeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle make not found with id: " + id));

        if (!vehicleMake.getName().equalsIgnoreCase(vehicleMakeDetails.getName())) {
            if (vehicleMakeRepository.existsByNameIgnoreCase(vehicleMakeDetails.getName())) {
                throw new IllegalArgumentException("Vehicle make with name '" + vehicleMakeDetails.getName() + "' already exists");
            }
        }

        vehicleMake.setName(vehicleMakeDetails.getName());
        vehicleMake.setCountry(vehicleMakeDetails.getCountry());
        vehicleMake.setIsActive(vehicleMakeDetails.getIsActive());
        vehicleMake.setDisplayOrder(vehicleMakeDetails.getDisplayOrder());

        return vehicleMakeRepository.save(vehicleMake);
    }

    @Transactional
    public void deleteVehicleMake(String id) {
        VehicleMake vehicleMake = vehicleMakeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle make not found with id: " + id));
        vehicleMake.setIsActive(false);
        vehicleMakeRepository.save(vehicleMake);
    }

    @Transactional
    public void hardDeleteVehicleMake(String id) {
        if (!vehicleMakeRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehicle make not found with id: " + id);
        }
        vehicleMakeRepository.deleteById(id);
    }

    @Transactional
    public VehicleMake activateVehicleMake(String id) {
        VehicleMake vehicleMake = vehicleMakeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle make not found with id: " + id));
        vehicleMake.setIsActive(true);
        return vehicleMakeRepository.save(vehicleMake);
    }
}
