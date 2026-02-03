package com.api.feros.repository;

import com.api.feros.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    @Query("SELECT DISTINCT v FROM Vehicle v JOIN FETCH v.client JOIN FETCH v.vehicleType JOIN FETCH v.make JOIN FETCH v.model ORDER BY v.vehicleNumber")
    List<Vehicle> findAllWithDetails();

    @Query(value = "SELECT v FROM Vehicle v JOIN FETCH v.client JOIN FETCH v.vehicleType JOIN FETCH v.make JOIN FETCH v.model WHERE (:search IS NULL OR :search = '' OR LOWER(v.vehicleNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.chassisNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.make.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.model.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.vehicleType.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:clientId IS NULL OR :clientId = '' OR v.client.id = :clientId) AND (:activeOnly = false OR v.isArchived = false) AND (:status IS NULL OR :status = '' OR v.status = :status) ORDER BY v.vehicleNumber",
            countQuery = "SELECT COUNT(v) FROM Vehicle v WHERE (:search IS NULL OR :search = '' OR LOWER(v.vehicleNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.chassisNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.make.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.model.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.vehicleType.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:clientId IS NULL OR :clientId = '' OR v.client.id = :clientId) AND (:activeOnly = false OR v.isArchived = false) AND (:status IS NULL OR :status = '' OR v.status = :status)")
    Page<Vehicle> findAllWithSearchAndFilters(@Param("search") String search, @Param("clientId") String clientId, @Param("activeOnly") boolean activeOnly, @Param("status") String status, Pageable pageable);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.client JOIN FETCH v.vehicleType JOIN FETCH v.make JOIN FETCH v.model WHERE v.client.id = :clientId ORDER BY v.vehicleNumber")
    List<Vehicle> findByClientId(@Param("clientId") String clientId);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.client JOIN FETCH v.vehicleType JOIN FETCH v.make JOIN FETCH v.model WHERE v.client.id = :clientId AND v.isArchived = false ORDER BY v.vehicleNumber")
    List<Vehicle> findByClientIdAndIsArchivedFalse(@Param("clientId") String clientId);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.client JOIN FETCH v.vehicleType JOIN FETCH v.make JOIN FETCH v.model WHERE v.id = :id")
    Optional<Vehicle> findByIdWithDetails(@Param("id") String id);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.client JOIN FETCH v.vehicleType JOIN FETCH v.make JOIN FETCH v.model WHERE v.client.id = :clientId AND v.status = :status")
    List<Vehicle> findByClientIdAndStatus(@Param("clientId") String clientId, @Param("status") String status);

    Optional<Vehicle> findByClientIdAndVehicleNumberIgnoreCase(String clientId, String vehicleNumber);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Vehicle v WHERE v.client.id = :clientId AND LOWER(v.vehicleNumber) = LOWER(:vehicleNumber)")
    boolean existsByClientIdAndVehicleNumberIgnoreCase(@Param("clientId") String clientId, @Param("vehicleNumber") String vehicleNumber);
}
