package com.api.feros.repository;

import com.api.feros.entity.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, String> {

    @Query("SELECT vm FROM VehicleModel vm JOIN FETCH vm.make JOIN FETCH vm.vehicleType WHERE vm.isActive = true ORDER BY vm.displayOrder ASC, vm.name ASC")
    List<VehicleModel> findByIsActiveTrueOrderByDisplayOrderAsc();

    @Query("SELECT vm FROM VehicleModel vm JOIN FETCH vm.make JOIN FETCH vm.vehicleType ORDER BY vm.displayOrder ASC, vm.name ASC")
    List<VehicleModel> findAllWithMakeAndType();

    @Query("SELECT vm FROM VehicleModel vm JOIN FETCH vm.make JOIN FETCH vm.vehicleType WHERE vm.id = :id")
    Optional<VehicleModel> findByIdWithMakeAndType(@Param("id") String id);

    @Query("SELECT vm FROM VehicleModel vm JOIN FETCH vm.make JOIN FETCH vm.vehicleType WHERE vm.make.id = :makeId")
    List<VehicleModel> findByMakeId(@Param("makeId") String makeId);

    @Query("SELECT vm FROM VehicleModel vm JOIN FETCH vm.make JOIN FETCH vm.vehicleType WHERE vm.vehicleType.id = :vehicleTypeId")
    List<VehicleModel> findByVehicleTypeId(@Param("vehicleTypeId") String vehicleTypeId);

    @Query("SELECT vm FROM VehicleModel vm WHERE LOWER(vm.name) = LOWER(:name)")
    Optional<VehicleModel> findByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(vm) > 0 THEN true ELSE false END FROM VehicleModel vm WHERE LOWER(vm.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);
}
