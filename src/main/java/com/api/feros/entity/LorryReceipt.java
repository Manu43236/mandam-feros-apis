package com.api.feros.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "lorry_receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LorryReceipt {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "lr_number", length = 50, nullable = false)
    private String lrNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false, unique = true)
    private VehicleAssignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private AppUser driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connection_id", nullable = false)
    private Connection connection;

    @Column(name = "pickup_location", columnDefinition = "TEXT", nullable = false)
    private String pickupLocation;

    @Column(name = "drop_location", columnDefinition = "TEXT", nullable = false)
    private String dropLocation;

    @Column(name = "material", length = 255, nullable = false)
    private String material;

    @Column(name = "loaded_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal loadedQuantity;

    @Column(name = "unit", length = 20, nullable = false)
    private String unit;

    @Column(name = "rate_type", nullable = false, columnDefinition = "ENUM('PER_QUANTITY','PER_TRIP')")
    private String rateType;

    @Column(name = "rate_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal rateValue;

    @Column(name = "freight_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal freightAmount;

    @Column(name = "detention_charges", precision = 10, scale = 2)
    private BigDecimal detentionCharges = BigDecimal.ZERO;

    @Column(name = "loading_charges", precision = 10, scale = 2)
    private BigDecimal loadingCharges = BigDecimal.ZERO;

    @Column(name = "unloading_charges", precision = 10, scale = 2)
    private BigDecimal unloadingCharges = BigDecimal.ZERO;

    @Column(name = "other_charges", precision = 10, scale = 2)
    private BigDecimal otherCharges = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "lr_date", nullable = false)
    private LocalDate lrDate;

    @Column(name = "loading_date", nullable = false)
    private LocalDate loadingDate;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDate actualDeliveryDate;

    @Column(name = "pod_received_date")
    private LocalDate podReceivedDate;

    @Column(name = "consignment_note", length = 100)
    private String consignmentNote;

    @Column(name = "eway_bill_no", length = 100)
    private String ewayBillNo;

    @Column(name = "reference_no", length = 100)
    private String referenceNo;

    @Column(name = "pickup_contact", columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> pickupContact;

    @Column(name = "drop_contact", columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> dropContact;

    @Column(name = "status", columnDefinition = "ENUM('DRAFT','CREATED','LOADED','IN_TRANSIT','DELIVERED','POD_PENDING','POD_RECEIVED','BILLED')")
    private String status = "DRAFT";

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "billed_at")
    private LocalDateTime billedAt;

    @Column(name = "invoice_id", length = 36)
    private String invoiceId;

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
            this.status = "DRAFT";
        }
        if (this.detentionCharges == null) this.detentionCharges = BigDecimal.ZERO;
        if (this.loadingCharges == null) this.loadingCharges = BigDecimal.ZERO;
        if (this.unloadingCharges == null) this.unloadingCharges = BigDecimal.ZERO;
        if (this.otherCharges == null) this.otherCharges = BigDecimal.ZERO;
    }
}
