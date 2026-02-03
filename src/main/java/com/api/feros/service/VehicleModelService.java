package com.api.feros.service;

import com.api.feros.entity.VehicleModel;
import com.api.feros.entity.VehicleMake;
import com.api.feros.entity.VehicleType;
import com.api.feros.repository.VehicleModelRepository;
import com.api.feros.repository.VehicleMakeRepository;
import com.api.feros.repository.VehicleTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleMakeRepository vehicleMakeRepository;
    private final VehicleTypeRepository vehicleTypeRepository;

    public Page<VehicleModel> getAllVehicleModelsPaged(boolean activeOnly, String makeId, String vehicleTypeId, Pageable pageable) {
        return vehicleModelRepository.findAllWithFilters(activeOnly, makeId, vehicleTypeId, pageable);
    }

    public List<VehicleModel> getAllVehicleModels() {
        return vehicleModelRepository.findAllWithMakeAndType();
    }

    public List<VehicleModel> getAllActiveVehicleModels() {
        return vehicleModelRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    public Optional<VehicleModel> getVehicleModelById(String id) {
        return vehicleModelRepository.findByIdWithMakeAndType(id);
    }

    @Transactional
    public VehicleModel createVehicleModel(VehicleModel vehicleModel) {
        if (vehicleModelRepository.existsByNameIgnoreCase(vehicleModel.getName())) {
            throw new IllegalArgumentException("Vehicle model with name '" + vehicleModel.getName() + "' already exists");
        }

        VehicleMake make = vehicleMakeRepository.findById(vehicleModel.getMake().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle make not found with id: " + vehicleModel.getMake().getId()));
        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleModel.getVehicleType().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type not found with id: " + vehicleModel.getVehicleType().getId()));

        vehicleModel.setMake(make);
        vehicleModel.setVehicleType(vehicleType);
        VehicleModel saved = vehicleModelRepository.save(vehicleModel);
        return vehicleModelRepository.findByIdWithMakeAndType(saved.getId()).orElse(saved);
    }

    @Transactional
    public VehicleModel updateVehicleModel(String id, VehicleModel vehicleModelDetails) {
        VehicleModel vehicleModel = vehicleModelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle model not found with id: " + id));

        if (!vehicleModel.getName().equalsIgnoreCase(vehicleModelDetails.getName())) {
            if (vehicleModelRepository.existsByNameIgnoreCase(vehicleModelDetails.getName())) {
                throw new IllegalArgumentException("Vehicle model with name '" + vehicleModelDetails.getName() + "' already exists");
            }
        }

        VehicleMake make = vehicleMakeRepository.findById(vehicleModelDetails.getMake().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle make not found with id: " + vehicleModelDetails.getMake().getId()));
        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleModelDetails.getVehicleType().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type not found with id: " + vehicleModelDetails.getVehicleType().getId()));

        vehicleModel.setName(vehicleModelDetails.getName());
        vehicleModel.setMake(make);
        vehicleModel.setVehicleType(vehicleType);
        vehicleModel.setTypicalCapacityTons(vehicleModelDetails.getTypicalCapacityTons());
        vehicleModel.setTypicalTyreCount(vehicleModelDetails.getTypicalTyreCount());
        vehicleModel.setIsActive(vehicleModelDetails.getIsActive());
        vehicleModel.setDisplayOrder(vehicleModelDetails.getDisplayOrder());

        VehicleModel saved = vehicleModelRepository.save(vehicleModel);
        return vehicleModelRepository.findByIdWithMakeAndType(saved.getId()).orElse(saved);
    }

    @Transactional
    public void deleteVehicleModel(String id) {
        VehicleModel vehicleModel = vehicleModelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle model not found with id: " + id));
        vehicleModel.setIsActive(false);
        vehicleModelRepository.save(vehicleModel);
    }

    @Transactional
    public void hardDeleteVehicleModel(String id) {
        if (!vehicleModelRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehicle model not found with id: " + id);
        }
        vehicleModelRepository.deleteById(id);
    }

    @Transactional
    public VehicleModel activateVehicleModel(String id) {
        VehicleModel vehicleModel = vehicleModelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle model not found with id: " + id));
        vehicleModel.setIsActive(true);
        return vehicleModelRepository.save(vehicleModel);
    }

    public List<VehicleModel> getVehicleModelsByMakeId(String makeId) {
        return vehicleModelRepository.findByMakeId(makeId);
    }

    public List<VehicleModel> getVehicleModelsByVehicleTypeId(String vehicleTypeId) {
        return vehicleModelRepository.findByVehicleTypeId(vehicleTypeId);
    }
}
