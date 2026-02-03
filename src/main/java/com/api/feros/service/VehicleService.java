package com.api.feros.service;

import com.api.feros.entity.*;
import com.api.feros.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleMakeRepository vehicleMakeRepository;
    private final VehicleModelRepository vehicleModelRepository;

    public Page<Vehicle> getAllVehiclesWithSearchAndFilters(String search, String clientId, boolean activeOnly, String status, Pageable pageable) {
        return vehicleRepository.findAllWithSearchAndFilters(search, clientId, activeOnly, status, pageable);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAllWithDetails();
    }

    public List<Vehicle> getVehiclesByClientId(String clientId) {
        return vehicleRepository.findByClientId(clientId);
    }

    public List<Vehicle> getActiveVehiclesByClientId(String clientId) {
        return vehicleRepository.findByClientIdAndIsArchivedFalse(clientId);
    }

    public List<Vehicle> getVehiclesByClientIdAndStatus(String clientId, String status) {
        return vehicleRepository.findByClientIdAndStatus(clientId, status);
    }

    public Optional<Vehicle> getVehicleById(String id) {
        return vehicleRepository.findByIdWithDetails(id);
    }

    @Transactional
    public Vehicle createVehicle(Vehicle vehicle) {
        Client client = clientRepository.findById(vehicle.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + vehicle.getClient().getId()));
        VehicleType vehicleType = vehicleTypeRepository.findById(vehicle.getVehicleType().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type not found with id: " + vehicle.getVehicleType().getId()));
        VehicleMake make = vehicleMakeRepository.findById(vehicle.getMake().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle make not found with id: " + vehicle.getMake().getId()));
        VehicleModel model = vehicleModelRepository.findById(vehicle.getModel().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle model not found with id: " + vehicle.getModel().getId()));

        if (vehicleRepository.existsByClientIdAndVehicleNumberIgnoreCase(client.getId(), vehicle.getVehicleNumber())) {
            throw new IllegalArgumentException("Vehicle with number '" + vehicle.getVehicleNumber() + "' already exists for this client");
        }

        vehicle.setClient(client);
        vehicle.setVehicleType(vehicleType);
        vehicle.setMake(make);
        vehicle.setModel(model);
        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleRepository.findByIdWithDetails(saved.getId()).orElse(saved);
    }

    @Transactional
    public Vehicle updateVehicle(String id, Vehicle vehicleDetails) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));

        if (!vehicle.getVehicleNumber().equalsIgnoreCase(vehicleDetails.getVehicleNumber())) {
            if (vehicleRepository.existsByClientIdAndVehicleNumberIgnoreCase(vehicle.getClient().getId(), vehicleDetails.getVehicleNumber())) {
                throw new IllegalArgumentException("Vehicle with number '" + vehicleDetails.getVehicleNumber() + "' already exists for this client");
            }
        }

        Client client = clientRepository.findById(vehicleDetails.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + vehicleDetails.getClient().getId()));
        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleDetails.getVehicleType().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type not found with id: " + vehicleDetails.getVehicleType().getId()));
        VehicleMake make = vehicleMakeRepository.findById(vehicleDetails.getMake().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle make not found with id: " + vehicleDetails.getMake().getId()));
        VehicleModel model = vehicleModelRepository.findById(vehicleDetails.getModel().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle model not found with id: " + vehicleDetails.getModel().getId()));

        vehicle.setClient(client);
        vehicle.setVehicleNumber(vehicleDetails.getVehicleNumber());
        vehicle.setVehicleType(vehicleType);
        vehicle.setMake(make);
        vehicle.setModel(model);
        vehicle.setYear(vehicleDetails.getYear());
        vehicle.setTyreCount(vehicleDetails.getTyreCount());
        vehicle.setCapacityTons(vehicleDetails.getCapacityTons());
        vehicle.setFuelType(vehicleDetails.getFuelType());
        vehicle.setChassisNumber(vehicleDetails.getChassisNumber());
        vehicle.setEngineNumber(vehicleDetails.getEngineNumber());
        vehicle.setRegistrationDate(vehicleDetails.getRegistrationDate());
        vehicle.setInsuranceExpiry(vehicleDetails.getInsuranceExpiry());
        vehicle.setPermitExpiry(vehicleDetails.getPermitExpiry());
        vehicle.setPucExpiry(vehicleDetails.getPucExpiry());
        vehicle.setFitnessExpiry(vehicleDetails.getFitnessExpiry());
        vehicle.setStatus(vehicleDetails.getStatus());
        vehicle.setNumberOfTripsCompleted(vehicleDetails.getNumberOfTripsCompleted());
        vehicle.setIsArchived(vehicleDetails.getIsArchived());
        vehicle.setUpdatedBy(vehicleDetails.getUpdatedBy());

        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleRepository.findByIdWithDetails(saved.getId()).orElse(saved);
    }

    @Transactional
    public void archiveVehicle(String id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));
        vehicle.setIsArchived(true);
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void unarchiveVehicle(String id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));
        vehicle.setIsArchived(false);
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void deleteVehicle(String id) {
        if (!vehicleRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }
}
