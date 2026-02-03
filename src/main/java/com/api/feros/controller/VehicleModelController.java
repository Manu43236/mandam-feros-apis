package com.api.feros.controller;

import com.api.feros.entity.VehicleModel;
import com.api.feros.service.VehicleModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle-models")
@RequiredArgsConstructor
public class VehicleModelController {

    private final VehicleModelService vehicleModelService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVehicleModels(
            @RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly,
            @RequestParam(value = "makeId", required = false) String makeId,
            @RequestParam(value = "vehicleTypeId", required = false) String vehicleTypeId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            var paged = vehicleModelService.getAllVehicleModelsPaged(activeOnly, makeId, vehicleTypeId, pageable);
            response.put("success", true);
            response.put("message", "Vehicle models retrieved successfully");
            response.put("data", paged.getContent());
            response.put("totalElements", paged.getTotalElements());
            response.put("totalPages", paged.getTotalPages());
            response.put("currentPage", paged.getNumber());
            response.put("pageSize", paged.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving vehicle models: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVehicleModelById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleModel vehicleModel = vehicleModelService.getVehicleModelById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle model not found with id: " + id));
            response.put("success", true);
            response.put("message", "Vehicle model retrieved successfully");
            response.put("data", vehicleModel);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving vehicle model: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createVehicleModel(@RequestBody VehicleModel vehicleModel) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleModel created = vehicleModelService.createVehicleModel(vehicleModel);
            response.put("success", true);
            response.put("message", "Vehicle model created successfully");
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating vehicle model: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateVehicleModel(
            @PathVariable String id,
            @RequestBody VehicleModel vehicleModel) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleModel updated = vehicleModelService.updateVehicleModel(id, vehicleModel);
            response.put("success", true);
            response.put("message", "Vehicle model updated successfully");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating vehicle model: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteVehicleModel(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            vehicleModelService.deleteVehicleModel(id);
            response.put("success", true);
            response.put("message", "Vehicle model deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting vehicle model: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activateVehicleModel(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleModel activated = vehicleModelService.activateVehicleModel(id);
            response.put("success", true);
            response.put("message", "Vehicle model activated successfully");
            response.put("data", activated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error activating vehicle model: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> hardDeleteVehicleModel(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            vehicleModelService.hardDeleteVehicleModel(id);
            response.put("success", true);
            response.put("message", "Vehicle model deleted permanently");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting vehicle model: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
