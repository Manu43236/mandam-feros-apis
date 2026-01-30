package com.api.feros.repository;

import com.api.feros.entity.VehicleMake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleMakeRepository extends JpaRepository<VehicleMake, String> {

    List<VehicleMake> findByIsActiveTrueOrderByDisplayOrderAsc();

    Optional<VehicleMake> findByName(String name);

    Optional<VehicleMake> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
