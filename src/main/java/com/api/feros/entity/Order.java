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
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connection_id", nullable = false)
    private Connection connection;

    @Column(name = "pickup_location", columnDefinition = "TEXT", nullable = false)
    private String pickupLocation;

    @Column(name = "drop_location", columnDefinition = "TEXT", nullable = false)
    private String dropLocation;

    @Column(name = "material", length = 255, nullable = false)
    private String material;

    @Column(name = "total_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalQuantity;

    @Column(name = "unit", nullable = false, columnDefinition = "ENUM('TONS','MT','KG','QUINTAL','CUBIC_METER','CUBIC_FEET','LITER','UNITS','BOXES','CARTONS','PALLETS','BAGS','DRUMS','ROLLS','BUNDLES','CONTAINERS','CRATES','OTHER')")
    private String unit;

    @Column(name = "rate_type", nullable = false, columnDefinition = "ENUM('PER_QUANTITY','PER_TRIP')")
    private String rateType;

    @Column(name = "rate_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal rateValue;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "expected_pickup_date", nullable = false)
    private LocalDate expectedPickupDate;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Column(name = "reference_no", length = 100)
    private String referenceNo;

    @Column(name = "pickup_contact", columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> pickupContact;

    @Column(name = "drop_contact", columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> dropContact;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "status", columnDefinition = "ENUM('DRAFT','CONFIRMED','IN_TRANSIT','DELIVERED','CANCELLED')")
    private String status = "DRAFT";

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
    }
}
