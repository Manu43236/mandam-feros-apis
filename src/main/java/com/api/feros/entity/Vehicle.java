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
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "vehicle_number", length = 50, nullable = false)
    private String vehicleNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_type_id", nullable = false)
    private VehicleType vehicleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "make_id", nullable = false)
    private VehicleMake make;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private VehicleModel model;

    @Column(name = "year")
    private Integer year;

    @Column(name = "tyre_count", nullable = false)
    private Integer tyreCount;

    @Column(name = "capacity_tons", nullable = false, precision = 10, scale = 2)
    private BigDecimal capacityTons;

    @Column(name = "fuel_type", length = 8, nullable = false)
    private String fuelType;

    @Column(name = "chassis_number", length = 100)
    private String chassisNumber;

    @Column(name = "engine_number", length = 100)
    private String engineNumber;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "insurance_expiry")
    private LocalDate insuranceExpiry;

    @Column(name = "permit_expiry")
    private LocalDate permitExpiry;

    @Column(name = "puc_expiry")
    private LocalDate pucExpiry;

    @Column(name = "fitness_expiry")
    private LocalDate fitnessExpiry;

    @Column(name = "status", length = 11, columnDefinition = "ENUM('AVAILABLE','IN_USE','MAINTENANCE','OUT_OF_SERVICE') DEFAULT 'AVAILABLE'")
    private String status = "AVAILABLE";

    @Column(name = "number_of_trips_completed", columnDefinition = "INT DEFAULT 0")
    private Integer numberOfTripsCompleted = 0;

    @Column(name = "is_archived", columnDefinition = "BIT DEFAULT 0")
    private Boolean isArchived = false;

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
            this.status = "AVAILABLE";
        }
        if (this.isArchived == null) {
            this.isArchived = false;
        }
        if (this.numberOfTripsCompleted == null) {
            this.numberOfTripsCompleted = 0;
        }
    }
}
