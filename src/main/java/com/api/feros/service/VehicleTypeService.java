package com.api.feros.service;

import com.api.feros.entity.VehicleType;
import com.api.feros.repository.VehicleTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleTypeService {

    private final VehicleTypeRepository vehicleTypeRepository;

    // Get all vehicle types
    public List<VehicleType> getAllVehicleTypes() {
        return vehicleTypeRepository.findAll();
    }

    // Get all active vehicle types (sorted by display order)
    public List<VehicleType> getAllActiveVehicleTypes() {
        return vehicleTypeRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    // Get vehicle type by ID
    public Optional<VehicleType> getVehicleTypeById(String id) {
        return vehicleTypeRepository.findById(id);
    }

    // Create new vehicle type
    @Transactional
    public VehicleType createVehicleType(VehicleType vehicleType) {
        // Check if name already exists
        if (vehicleTypeRepository.existsByNameIgnoreCase(vehicleType.getName())) {
            throw new IllegalArgumentException("Vehicle type with name '" + vehicleType.getName() + "' already exists");
        }
        return vehicleTypeRepository.save(vehicleType);
    }

    // Update vehicle type
    @Transactional
    public VehicleType updateVehicleType(String id, VehicleType vehicleTypeDetails) {
        VehicleType vehicleType = vehicleTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!vehicleType.getName().equalsIgnoreCase(vehicleTypeDetails.getName())) {
            if (vehicleTypeRepository.existsByNameIgnoreCase(vehicleTypeDetails.getName())) {
                throw new IllegalArgumentException("Vehicle type with name '" + vehicleTypeDetails.getName() + "' already exists");
            }
        }

        vehicleType.setName(vehicleTypeDetails.getName());
        vehicleType.setDescription(vehicleTypeDetails.getDescription());
        vehicleType.setIsActive(vehicleTypeDetails.getIsActive());
        vehicleType.setDisplayOrder(vehicleTypeDetails.getDisplayOrder());

        return vehicleTypeRepository.save(vehicleType);
    }

    // Delete vehicle type (soft delete by setting isActive to false)
    @Transactional
    public void deleteVehicleType(String id) {
        VehicleType vehicleType = vehicleTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type not found with id: " + id));
        
        vehicleType.setIsActive(false);
        vehicleTypeRepository.save(vehicleType);
    }

    // Hard delete vehicle type (permanent deletion)
    @Transactional
    public void hardDeleteVehicleType(String id) {
        if (!vehicleTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehicle type not found with id: " + id);
        }
        vehicleTypeRepository.deleteById(id);
    }

    // Activate vehicle type
    @Transactional
    public VehicleType activateVehicleType(String id) {
        VehicleType vehicleType = vehicleTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type not found with id: " + id));
        
        vehicleType.setIsActive(true);
        return vehicleTypeRepository.save(vehicleType);
    }
}
