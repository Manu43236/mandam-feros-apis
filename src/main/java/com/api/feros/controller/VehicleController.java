package com.api.feros.controller;

import com.api.feros.entity.Vehicle;
import com.api.feros.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVehicles(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            var paged = vehicleService.getAllVehiclesWithSearchAndFilters(search, clientId, activeOnly, status, pageable);
            response.put("success", true);
            response.put("message", "Vehicles retrieved successfully");
            response.put("data", paged.getContent());
            response.put("totalElements", paged.getTotalElements());
            response.put("totalPages", paged.getTotalPages());
            response.put("currentPage", paged.getNumber());
            response.put("pageSize", paged.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving vehicles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVehicleById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Vehicle vehicle = vehicleService.getVehicleById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));
            response.put("success", true);
            response.put("message", "Vehicle retrieved successfully");
            response.put("data", vehicle);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving vehicle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createVehicle(@RequestBody Vehicle vehicle) {
        Map<String, Object> response = new HashMap<>();
        try {
            Vehicle created = vehicleService.createVehicle(vehicle);
            response.put("success", true);
            response.put("message", "Vehicle created successfully");
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating vehicle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateVehicle(
            @PathVariable String id,
            @RequestBody Vehicle vehicle) {
        Map<String, Object> response = new HashMap<>();
        try {
            Vehicle updated = vehicleService.updateVehicle(id, vehicle);
            response.put("success", true);
            response.put("message", "Vehicle updated successfully");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating vehicle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Map<String, Object>> archiveVehicle(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            vehicleService.archiveVehicle(id);
            response.put("success", true);
            response.put("message", "Vehicle archived successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error archiving vehicle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/unarchive")
    public ResponseEntity<Map<String, Object>> unarchiveVehicle(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            vehicleService.unarchiveVehicle(id);
            response.put("success", true);
            response.put("message", "Vehicle unarchived successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error unarchiving vehicle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteVehicle(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            vehicleService.deleteVehicle(id);
            response.put("success", true);
            response.put("message", "Vehicle deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting vehicle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
