package com.api.feros.controller;

import com.api.feros.entity.VehicleType;
import com.api.feros.service.VehicleTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle-types")
@RequiredArgsConstructor
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVehicleTypes(
            @RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            var paged = vehicleTypeService.getAllVehicleTypesPaged(activeOnly, pageable);
            response.put("success", true);
            response.put("message", "Vehicle types retrieved successfully");
            response.put("data", paged.getContent());
            response.put("totalElements", paged.getTotalElements());
            response.put("totalPages", paged.getTotalPages());
            response.put("currentPage", paged.getNumber());
            response.put("pageSize", paged.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving vehicle types: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get vehicle type by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVehicleTypeById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleType vehicleType = vehicleTypeService.getVehicleTypeById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle type not found with id: " + id));
            
            response.put("success", true);
            response.put("message", "Vehicle type retrieved successfully");
            response.put("data", vehicleType);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving vehicle type: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Create new vehicle type
    @PostMapping
    public ResponseEntity<Map<String, Object>> createVehicleType(@RequestBody VehicleType vehicleType) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleType createdVehicleType = vehicleTypeService.createVehicleType(vehicleType);
            
            response.put("success", true);
            response.put("message", "Vehicle type created successfully");
            response.put("data", createdVehicleType);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating vehicle type: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Update vehicle type
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateVehicleType(
            @PathVariable String id, 
            @RequestBody VehicleType vehicleType) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleType updatedVehicleType = vehicleTypeService.updateVehicleType(id, vehicleType);
            
            response.put("success", true);
            response.put("message", "Vehicle type updated successfully");
            response.put("data", updatedVehicleType);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating vehicle type: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Soft delete vehicle type
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteVehicleType(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            vehicleTypeService.deleteVehicleType(id);
            
            response.put("success", true);
            response.put("message", "Vehicle type deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting vehicle type: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Activate vehicle type
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activateVehicleType(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleType activatedVehicleType = vehicleTypeService.activateVehicleType(id);
            
            response.put("success", true);
            response.put("message", "Vehicle type activated successfully");
            response.put("data", activatedVehicleType);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error activating vehicle type: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Hard delete vehicle type (permanent)
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> hardDeleteVehicleType(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            vehicleTypeService.hardDeleteVehicleType(id);
            
            response.put("success", true);
            response.put("message", "Vehicle type deleted permanently");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting vehicle type: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
