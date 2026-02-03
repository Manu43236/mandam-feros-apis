package com.api.feros.controller;

import com.api.feros.entity.LorryReceipt;
import com.api.feros.service.LorryReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/lorry-receipts")
@RequiredArgsConstructor
public class LorryReceiptController {

    private final LorryReceiptService lorryReceiptService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLorryReceipts(
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "assignmentId", required = false) String assignmentId,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (assignmentId != null && !assignmentId.isEmpty()) {
                java.util.List<LorryReceipt> list = lorryReceiptService.getLorryReceiptByAssignmentId(assignmentId)
                        .map(java.util.List::of).orElse(java.util.List.of());
                response.put("success", true);
                response.put("message", "Lorry receipts retrieved successfully");
                response.put("data", list);
                response.put("totalElements", (long) list.size());
                response.put("totalPages", list.isEmpty() ? 0 : 1);
                response.put("currentPage", 0);
                response.put("pageSize", size);
                return ResponseEntity.ok(response);
            }
            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            var paged = lorryReceiptService.getLorryReceiptsPaged(clientId, status, orderId, fromDate, toDate, pageable);
            response.put("success", true);
            response.put("message", "Lorry receipts retrieved successfully");
            response.put("data", paged.getContent());
            response.put("totalElements", paged.getTotalElements());
            response.put("totalPages", paged.getTotalPages());
            response.put("currentPage", paged.getNumber());
            response.put("pageSize", paged.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving lorry receipts: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getLorryReceiptById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            LorryReceipt lr = lorryReceiptService.getLorryReceiptById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Lorry receipt not found with id: " + id));
            response.put("success", true);
            response.put("message", "Lorry receipt retrieved successfully");
            response.put("data", lr);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving lorry receipt: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createLorryReceipt(@RequestBody LorryReceipt lorryReceipt) {
        Map<String, Object> response = new HashMap<>();
        try {
            LorryReceipt created = lorryReceiptService.createLorryReceipt(lorryReceipt);
            response.put("success", true);
            response.put("message", "Lorry receipt created successfully");
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating lorry receipt: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateLorryReceipt(
            @PathVariable String id,
            @RequestBody LorryReceipt lorryReceipt) {
        Map<String, Object> response = new HashMap<>();
        try {
            LorryReceipt updated = lorryReceiptService.updateLorryReceipt(id, lorryReceipt);
            response.put("success", true);
            response.put("message", "Lorry receipt updated successfully");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating lorry receipt: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateLorryReceiptStatus(
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
            LorryReceipt updated = lorryReceiptService.updateLorryReceiptStatus(id, status);
            response.put("success", true);
            response.put("message", "Lorry receipt status updated successfully");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating lorry receipt status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteLorryReceipt(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            lorryReceiptService.deleteLorryReceipt(id);
            response.put("success", true);
            response.put("message", "Lorry receipt deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting lorry receipt: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
