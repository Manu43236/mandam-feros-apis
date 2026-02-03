package com.api.feros.repository;

import com.api.feros.entity.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, String> {

    List<VehicleType> findByIsActiveTrueOrderByDisplayOrderAsc();

    Page<VehicleType> findByIsActiveTrueOrderByDisplayOrderAsc(Pageable pageable);

    Page<VehicleType> findAllByOrderByDisplayOrderAsc(Pageable pageable);

    Optional<VehicleType> findByName(String name);

    Optional<VehicleType> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
