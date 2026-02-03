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
public class VehicleAssignmentService {

    private final VehicleAssignmentRepository assignmentRepository;
    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public Page<VehicleAssignment> getAllAssignmentsWithFilters(String clientId, String status, String orderId, String vehicleId, String driverId, Pageable pageable) {
        return assignmentRepository.findAllWithFilters(clientId, status, orderId, vehicleId, driverId, pageable);
    }

    public List<VehicleAssignment> getAllAssignments() {
        return assignmentRepository.findAllWithDetails();
    }

    public List<VehicleAssignment> getAssignmentsByClientId(String clientId) {
        return assignmentRepository.findByClientId(clientId);
    }

    public List<VehicleAssignment> getAssignmentsByClientIdAndStatus(String clientId, String status) {
        return assignmentRepository.findByClientIdAndStatus(clientId, status);
    }

    public List<VehicleAssignment> getAssignmentsByOrderId(String orderId) {
        return assignmentRepository.findByOrderId(orderId);
    }

    public List<VehicleAssignment> getAssignmentsByVehicleId(String vehicleId) {
        return assignmentRepository.findByVehicleId(vehicleId);
    }

    public List<VehicleAssignment> getAssignmentsByDriverId(String driverId) {
        return assignmentRepository.findByDriverId(driverId);
    }

    public Optional<VehicleAssignment> getAssignmentById(String id) {
        return assignmentRepository.findByIdWithDetails(id);
    }

    @Transactional
    public VehicleAssignment createAssignment(VehicleAssignment assignment) {
        Order order = orderRepository.findByIdWithClientAndConnection(assignment.getOrder().getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + assignment.getOrder().getId()));
        Vehicle vehicle = vehicleRepository.findByIdWithDetails(assignment.getVehicle().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + assignment.getVehicle().getId()));
        AppUser driver = userRepository.findByIdWithClient(assignment.getDriver().getId())
                .orElseThrow(() -> new IllegalArgumentException("Driver (user) not found with id: " + assignment.getDriver().getId()));

        if (!order.getClient().getId().equals(vehicle.getClient().getId())) {
            throw new IllegalArgumentException("Vehicle must belong to the same client as the order");
        }
        if (!order.getClient().getId().equals(driver.getClient().getId())) {
            throw new IllegalArgumentException("Driver must belong to the same client as the order");
        }

        assignment.setClient(order.getClient());
        assignment.setOrder(order);
        assignment.setVehicle(vehicle);
        assignment.setDriver(driver);
        VehicleAssignment saved = assignmentRepository.save(assignment);
        return assignmentRepository.findByIdWithDetails(saved.getId()).orElse(saved);
    }

    @Transactional
    public VehicleAssignment updateAssignment(String id, VehicleAssignment assignmentDetails) {
        VehicleAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle assignment not found with id: " + id));

        Order order = orderRepository.findByIdWithClientAndConnection(assignmentDetails.getOrder().getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + assignmentDetails.getOrder().getId()));
        Vehicle vehicle = vehicleRepository.findByIdWithDetails(assignmentDetails.getVehicle().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + assignmentDetails.getVehicle().getId()));
        AppUser driver = userRepository.findByIdWithClient(assignmentDetails.getDriver().getId())
                .orElseThrow(() -> new IllegalArgumentException("Driver (user) not found with id: " + assignmentDetails.getDriver().getId()));

        if (!order.getClient().getId().equals(vehicle.getClient().getId()) || !order.getClient().getId().equals(driver.getClient().getId())) {
            throw new IllegalArgumentException("Order, vehicle and driver must belong to the same client");
        }

        assignment.setClient(order.getClient());
        assignment.setOrder(order);
        assignment.setVehicle(vehicle);
        assignment.setDriver(driver);
        assignment.setPlannedQuantity(assignmentDetails.getPlannedQuantity());
        assignment.setUnit(assignmentDetails.getUnit());
        assignment.setExpectedDate(assignmentDetails.getExpectedDate());
        assignment.setNotes(assignmentDetails.getNotes());
        assignment.setStatus(assignmentDetails.getStatus());
        assignment.setUpdatedBy(assignmentDetails.getUpdatedBy());

        VehicleAssignment saved = assignmentRepository.save(assignment);
        return assignmentRepository.findByIdWithDetails(saved.getId()).orElse(saved);
    }

    @Transactional
    public VehicleAssignment updateAssignmentStatus(String id, String status) {
        VehicleAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle assignment not found with id: " + id));
        assignment.setStatus(status);
        VehicleAssignment saved = assignmentRepository.save(assignment);
        return assignmentRepository.findByIdWithDetails(saved.getId()).orElse(saved);
    }

    @Transactional
    public void deleteAssignment(String id) {
        if (!assignmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehicle assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }
}
