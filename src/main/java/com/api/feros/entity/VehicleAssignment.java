package com.api.feros.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleAssignment {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private AppUser driver;

    @Column(name = "planned_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal plannedQuantity;

    @Column(name = "unit", length = 20, nullable = false)
    private String unit;

    @Column(name = "expected_date", nullable = false)
    private LocalDate expectedDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "status", columnDefinition = "ENUM('ASSIGNED','COMPLETED','CANCELLED')")
    private String status = "ASSIGNED";

    @Column(name = "reassignment_reason", columnDefinition = "TEXT")
    private String reassignmentReason;

    @Column(name = "reassigned_at")
    private LocalDateTime reassignedAt;

    @Column(name = "reassigned_by", length = 36)
    private String reassignedBy;

    @Column(name = "lr_id", length = 36)
    private String lrId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 36)
    private String createdBy;

    @Column(name = "updated_by", length = 36)
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = java.util.UUID.randomUUID().toString();
        }
        if (this.status == null) {
            this.status = "ASSIGNED";
        }
    }
}
