package com.api.feros.repository;

import com.api.feros.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, String> {
    
    // Find all active vehicle types
    List<VehicleType> findByIsActiveTrueOrderByDisplayOrderAsc();
    
    // Find by name
    Optional<VehicleType> findByName(String name);
    
    // Find by name (case-insensitive)
    Optional<VehicleType> findByNameIgnoreCase(String name);
    
    // Check if name exists
    boolean existsByNameIgnoreCase(String name);
}
