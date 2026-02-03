package com.api.feros.repository;

import com.api.feros.entity.VehicleAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleAssignmentRepository extends JpaRepository<VehicleAssignment, String> {

    @Query("SELECT DISTINCT va FROM VehicleAssignment va JOIN FETCH va.client JOIN FETCH va.order JOIN FETCH va.vehicle JOIN FETCH va.driver ORDER BY va.expectedDate DESC, va.createdAt DESC")
    List<VehicleAssignment> findAllWithDetails();

    @Query(value = "SELECT va FROM VehicleAssignment va JOIN FETCH va.client JOIN FETCH va.order JOIN FETCH va.vehicle JOIN FETCH va.driver WHERE (:clientId IS NULL OR :clientId = '' OR va.client.id = :clientId) AND (:status IS NULL OR :status = '' OR va.status = :status) AND (:orderId IS NULL OR :orderId = '' OR va.order.id = :orderId) AND (:vehicleId IS NULL OR :vehicleId = '' OR va.vehicle.id = :vehicleId) AND (:driverId IS NULL OR :driverId = '' OR va.driver.id = :driverId) ORDER BY va.expectedDate DESC, va.createdAt DESC",
            countQuery = "SELECT COUNT(va) FROM VehicleAssignment va WHERE (:clientId IS NULL OR :clientId = '' OR va.client.id = :clientId) AND (:status IS NULL OR :status = '' OR va.status = :status) AND (:orderId IS NULL OR :orderId = '' OR va.order.id = :orderId) AND (:vehicleId IS NULL OR :vehicleId = '' OR va.vehicle.id = :vehicleId) AND (:driverId IS NULL OR :driverId = '' OR va.driver.id = :driverId)")
    Page<VehicleAssignment> findAllWithFilters(@Param("clientId") String clientId, @Param("status") String status, @Param("orderId") String orderId, @Param("vehicleId") String vehicleId, @Param("driverId") String driverId, Pageable pageable);

    @Query("SELECT va FROM VehicleAssignment va JOIN FETCH va.client JOIN FETCH va.order JOIN FETCH va.vehicle JOIN FETCH va.driver WHERE va.client.id = :clientId ORDER BY va.expectedDate DESC")
    List<VehicleAssignment> findByClientId(@Param("clientId") String clientId);

    @Query("SELECT va FROM VehicleAssignment va JOIN FETCH va.client JOIN FETCH va.order JOIN FETCH va.vehicle JOIN FETCH va.driver WHERE va.client.id = :clientId AND va.status = :status ORDER BY va.expectedDate DESC")
    List<VehicleAssignment> findByClientIdAndStatus(@Param("clientId") String clientId, @Param("status") String status);

    @Query("SELECT va FROM VehicleAssignment va JOIN FETCH va.client JOIN FETCH va.order JOIN FETCH va.vehicle JOIN FETCH va.driver WHERE va.order.id = :orderId ORDER BY va.createdAt DESC")
    List<VehicleAssignment> findByOrderId(@Param("orderId") String orderId);

    @Query("SELECT va FROM VehicleAssignment va JOIN FETCH va.client JOIN FETCH va.order JOIN FETCH va.vehicle JOIN FETCH va.driver WHERE va.vehicle.id = :vehicleId ORDER BY va.expectedDate DESC")
    List<VehicleAssignment> findByVehicleId(@Param("vehicleId") String vehicleId);

    @Query("SELECT va FROM VehicleAssignment va JOIN FETCH va.client JOIN FETCH va.order JOIN FETCH va.vehicle JOIN FETCH va.driver WHERE va.driver.id = :driverId ORDER BY va.expectedDate DESC")
    List<VehicleAssignment> findByDriverId(@Param("driverId") String driverId);

    @Query("SELECT va FROM VehicleAssignment va JOIN FETCH va.client JOIN FETCH va.order JOIN FETCH va.vehicle JOIN FETCH va.driver WHERE va.id = :id")
    Optional<VehicleAssignment> findByIdWithDetails(@Param("id") String id);
}
