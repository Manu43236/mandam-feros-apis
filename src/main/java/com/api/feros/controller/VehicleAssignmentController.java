package com.api.feros.controller;

import com.api.feros.entity.VehicleAssignment;
import com.api.feros.service.VehicleAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle-assignments")
@RequiredArgsConstructor
public class VehicleAssignmentController {

    private final VehicleAssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAssignments(
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "vehicleId", required = false) String vehicleId,
            @RequestParam(value = "driverId", required = false) String driverId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            var paged = assignmentService.getAllAssignmentsWithFilters(clientId, status, orderId, vehicleId, driverId, pageable);
            response.put("success", true);
            response.put("message", "Vehicle assignments retrieved successfully");
            response.put("data", paged.getContent());
            response.put("totalElements", paged.getTotalElements());
            response.put("totalPages", paged.getTotalPages());
            response.put("currentPage", paged.getNumber());
            response.put("pageSize", paged.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving assignments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAssignmentById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleAssignment assignment = assignmentService.getAssignmentById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle assignment not found with id: " + id));
            response.put("success", true);
            response.put("message", "Vehicle assignment retrieved successfully");
            response.put("data", assignment);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAssignment(@RequestBody VehicleAssignment assignment) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleAssignment created = assignmentService.createAssignment(assignment);
            response.put("success", true);
            response.put("message", "Vehicle assignment created successfully (Order → Vehicle → Driver)");
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAssignment(
            @PathVariable String id,
            @RequestBody VehicleAssignment assignment) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleAssignment updated = assignmentService.updateAssignment(id, assignment);
            response.put("success", true);
            response.put("message", "Vehicle assignment updated successfully");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateAssignmentStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        Map<String, Object> response = new HashMap<>();
        try {
            String status = body.get("status");
            if (status == null || status.isBlank()) {
                response.put("success", false);
                response.put("message", "status is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            VehicleAssignment updated = assignmentService.updateAssignmentStatus(id, status);
            response.put("success", true);
            response.put("message", "Assignment status updated successfully");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAssignment(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            assignmentService.deleteAssignment(id);
            response.put("success", true);
            response.put("message", "Vehicle assignment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
