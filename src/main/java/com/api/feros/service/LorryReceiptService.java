package com.api.feros.service;

import com.api.feros.entity.*;
import com.api.feros.repository.ConnectionRepository;
import com.api.feros.repository.LorryReceiptRepository;
import com.api.feros.repository.OrderRepository;
import com.api.feros.repository.UserRepository;
import com.api.feros.repository.VehicleAssignmentRepository;
import com.api.feros.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LorryReceiptService {

    private final LorryReceiptRepository lorryReceiptRepository;
    private final VehicleAssignmentRepository vehicleAssignmentRepository;
    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;

    public Page<LorryReceipt> getLorryReceiptsPaged(String clientId, String status, String orderId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return lorryReceiptRepository.findAllWithFilters(clientId, status, orderId, fromDate, toDate, pageable);
    }

    public List<LorryReceipt> getAllLorryReceipts() {
        return lorryReceiptRepository.findAllWithDetails();
    }

    public List<LorryReceipt> getLorryReceiptsByClientId(String clientId) {
        return lorryReceiptRepository.findByClientId(clientId);
    }

    public List<LorryReceipt> getLorryReceiptsByClientIdAndStatus(String clientId, String status) {
        return lorryReceiptRepository.findByClientIdAndStatus(clientId, status);
    }

    public List<LorryReceipt> getLorryReceiptsByOrderId(String orderId) {
        return lorryReceiptRepository.findByOrderId(orderId);
    }

    public Optional<LorryReceipt> getLorryReceiptByAssignmentId(String assignmentId) {
        return lorryReceiptRepository.findByAssignmentId(assignmentId);
    }

    public List<LorryReceipt> getLorryReceiptsByClientIdAndDateRange(String clientId, LocalDate fromDate, LocalDate toDate) {
        return lorryReceiptRepository.findByClientIdAndLrDateBetween(clientId, fromDate, toDate);
    }

    public Optional<LorryReceipt> getLorryReceiptById(String id) {
        return lorryReceiptRepository.findByIdWithDetails(id);
    }

    @Transactional
    public LorryReceipt createLorryReceipt(LorryReceipt lr) {
        VehicleAssignment assignment = vehicleAssignmentRepository.findByIdWithDetails(lr.getAssignment().getId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found with id: " + lr.getAssignment().getId()));
        Order order = orderRepository.findByIdWithClientAndConnection(lr.getOrder().getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + lr.getOrder().getId()));
        Vehicle vehicle = vehicleRepository.findByIdWithDetails(lr.getVehicle().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + lr.getVehicle().getId()));
        AppUser driver = userRepository.findByIdWithClient(lr.getDriver().getId())
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + lr.getDriver().getId()));
        Connection connection = connectionRepository.findByIdWithClient(lr.getConnection().getId())
                .orElseThrow(() -> new IllegalArgumentException("Connection not found with id: " + lr.getConnection().getId()));

        if (!assignment.getOrder().getId().equals(order.getId())) {
            throw new IllegalArgumentException("Assignment does not belong to this order");
        }
        if (!assignment.getVehicle().getId().equals(vehicle.getId())) {
            throw new IllegalArgumentException("Assignment does not use this vehicle");
        }
        if (!assignment.getDriver().getId().equals(driver.getId())) {
            throw new IllegalArgumentException("Assignment does not use this driver");
        }
        if (!order.getClient().getId().equals(assignment.getClient().getId())) {
            throw new IllegalArgumentException("Order client and assignment client must match");
        }
        if (!connection.getClient().getId().equals(assignment.getClient().getId())) {
            throw new IllegalArgumentException("Connection does not belong to this client");
        }
        if (lorryReceiptRepository.findByAssignmentId(assignment.getId()).isPresent()) {
            throw new IllegalArgumentException("A lorry receipt already exists for this assignment");
        }

        Client client = assignment.getClient();
        lr.setClient(client);
        lr.setAssignment(assignment);
        lr.setOrder(order);
        lr.setVehicle(vehicle);
        lr.setDriver(driver);
        lr.setConnection(connection);

        LorryReceipt saved = lorryReceiptRepository.save(lr);
        return lorryReceiptRepository.findByIdWithDetails(saved.getId()).orElse(saved);
    }

    @Transactional
    public LorryReceipt updateLorryReceipt(String id, LorryReceipt details) {
        LorryReceipt lr = lorryReceiptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lorry receipt not found with id: " + id));

        lr.setLrNumber(details.getLrNumber());
        lr.setPickupLocation(details.getPickupLocation());
        lr.setDropLocation(details.getDropLocation());
        lr.setMaterial(details.getMaterial());
        lr.setLoadedQuantity(details.getLoadedQuantity());
        lr.setUnit(details.getUnit());
        lr.setRateType(details.getRateType());
        lr.setRateValue(details.getRateValue());
        lr.setFreightAmount(details.getFreightAmount());
        lr.setDetentionCharges(details.getDetentionCharges() != null ? details.getDetentionCharges() : java.math.BigDecimal.ZERO);
        lr.setLoadingCharges(details.getLoadingCharges() != null ? details.getLoadingCharges() : java.math.BigDecimal.ZERO);
        lr.setUnloadingCharges(details.getUnloadingCharges() != null ? details.getUnloadingCharges() : java.math.BigDecimal.ZERO);
        lr.setOtherCharges(details.getOtherCharges() != null ? details.getOtherCharges() : java.math.BigDecimal.ZERO);
        lr.setTotalAmount(details.getTotalAmount());
        lr.setLrDate(details.getLrDate());
        lr.setLoadingDate(details.getLoadingDate());
        lr.setExpectedDeliveryDate(details.getExpectedDeliveryDate());
        lr.setActualDeliveryDate(details.getActualDeliveryDate());
        lr.setPodReceivedDate(details.getPodReceivedDate());
        lr.setConsignmentNote(details.getConsignmentNote());
        lr.setEwayBillNo(details.getEwayBillNo());
        lr.setReferenceNo(details.getReferenceNo());
        lr.setPickupContact(details.getPickupContact());
        lr.setDropContact(details.getDropContact());
        lr.setStatus(details.getStatus() != null ? details.getStatus() : lr.getStatus());
        lr.setNotes(details.getNotes());
        lr.setUpdatedBy(details.getUpdatedBy());

        LorryReceipt saved = lorryReceiptRepository.save(lr);
        return lorryReceiptRepository.findByIdWithDetails(saved.getId()).orElse(saved);
    }

    @Transactional
    public LorryReceipt updateLorryReceiptStatus(String id, String status) {
        LorryReceipt lr = lorryReceiptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lorry receipt not found with id: " + id));
        lr.setStatus(status);
        LorryReceipt saved = lorryReceiptRepository.save(lr);
        return lorryReceiptRepository.findByIdWithDetails(saved.getId()).orElse(saved);
    }

    @Transactional
    public void deleteLorryReceipt(String id) {
        if (!lorryReceiptRepository.existsById(id)) {
            throw new IllegalArgumentException("Lorry receipt not found with id: " + id);
        }
        lorryReceiptRepository.deleteById(id);
    }
}
