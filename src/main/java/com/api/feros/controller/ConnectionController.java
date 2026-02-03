package com.api.feros.controller;

import com.api.feros.entity.Connection;
import com.api.feros.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllConnections(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            var paged = connectionService.getAllConnectionsWithSearchAndFilters(search, clientId, activeOnly, status, pageable);
            response.put("success", true);
            response.put("message", "Connections retrieved successfully");
            response.put("data", paged.getContent());
            response.put("totalElements", paged.getTotalElements());
            response.put("totalPages", paged.getTotalPages());
            response.put("currentPage", paged.getNumber());
            response.put("pageSize", paged.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving connections: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getConnectionById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Connection connection = connectionService.getConnectionById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Connection not found with id: " + id));
            response.put("success", true);
            response.put("message", "Connection retrieved successfully");
            response.put("data", connection);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving connection: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createConnection(@RequestBody Connection connection) {
        Map<String, Object> response = new HashMap<>();
        try {
            Connection created = connectionService.createConnection(connection);
            response.put("success", true);
            response.put("message", "Connection created successfully");
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating connection: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateConnection(
            @PathVariable String id,
            @RequestBody Connection connection) {
        Map<String, Object> response = new HashMap<>();
        try {
            Connection updated = connectionService.updateConnection(id, connection);
            response.put("success", true);
            response.put("message", "Connection updated successfully");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating connection: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Map<String, Object>> archiveConnection(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            connectionService.archiveConnection(id);
            response.put("success", true);
            response.put("message", "Connection archived successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error archiving connection: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/unarchive")
    public ResponseEntity<Map<String, Object>> unarchiveConnection(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            connectionService.unarchiveConnection(id);
            response.put("success", true);
            response.put("message", "Connection unarchived successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error unarchiving connection: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteConnection(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            connectionService.deleteConnection(id);
            response.put("success", true);
            response.put("message", "Connection deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting connection: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
