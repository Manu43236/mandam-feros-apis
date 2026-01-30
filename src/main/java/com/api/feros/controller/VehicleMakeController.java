package com.api.feros.controller;

import com.api.feros.entity.VehicleMake;
import com.api.feros.service.VehicleMakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle-makes")
@RequiredArgsConstructor
public class VehicleMakeController {

    private final VehicleMakeService vehicleMakeService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVehicleMakes(
            @RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<VehicleMake> vehicleMakes = activeOnly
                    ? vehicleMakeService.getAllActiveVehicleMakes()
                    : vehicleMakeService.getAllVehicleMakes();
            response.put("success", true);
            response.put("message", "Vehicle makes retrieved successfully");
            response.put("data", vehicleMakes);
            response.put("count", vehicleMakes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving vehicle makes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVehicleMakeById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleMake vehicleMake = vehicleMakeService.getVehicleMakeById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle make not found with id: " + id));
            response.put("success", true);
            response.put("message", "Vehicle make retrieved successfully");
            response.put("data", vehicleMake);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving vehicle make: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createVehicleMake(@RequestBody VehicleMake vehicleMake) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleMake created = vehicleMakeService.createVehicleMake(vehicleMake);
            response.put("success", true);
            response.put("message", "Vehicle make created successfully");
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating vehicle make: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateVehicleMake(
            @PathVariable String id,
            @RequestBody VehicleMake vehicleMake) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleMake updated = vehicleMakeService.updateVehicleMake(id, vehicleMake);
            response.put("success", true);
            response.put("message", "Vehicle make updated successfully");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating vehicle make: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteVehicleMake(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            vehicleMakeService.deleteVehicleMake(id);
            response.put("success", true);
            response.put("message", "Vehicle make deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting vehicle make: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activateVehicleMake(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleMake activated = vehicleMakeService.activateVehicleMake(id);
            response.put("success", true);
            response.put("message", "Vehicle make activated successfully");
            response.put("data", activated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error activating vehicle make: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> hardDeleteVehicleMake(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            vehicleMakeService.hardDeleteVehicleMake(id);
            response.put("success", true);
            response.put("message", "Vehicle make deleted permanently");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting vehicle make: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
