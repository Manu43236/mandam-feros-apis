package com.api.feros.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "company_trade_name", length = 255, nullable = false)
    private String companyTradeName;

    @Column(name = "business_type", length = 14, nullable = false)
    private String businessType;

    @Column(name = "company_logo", length = 500)
    private String companyLogo;

    @Column(name = "hq_city", length = 100, nullable = false)
    private String hqCity;

    @Column(name = "hq_state", length = 100, nullable = false)
    private String hqState;

    @Column(name = "hq_address_line", columnDefinition = "TEXT")
    private String hqAddressLine;

    @Column(name = "hq_pincode", length = 10)
    private String hqPincode;

    @Column(name = "primary_contact_name", length = 255, nullable = false)
    private String primaryContactName;

    @Column(name = "primary_phone", length = 20, nullable = false)
    private String primaryPhone;

    @Column(name = "primary_email", length = 255)
    private String primaryEmail;

    @Column(name = "secondary_phone", length = 20)
    private String secondaryPhone;

    @Column(name = "secondary_email", length = 255)
    private String secondaryEmail;

    @Column(name = "gst_registered", columnDefinition = "BIT DEFAULT 0")
    private Boolean gstRegistered = false;

    @Column(name = "gstin", length = 20)
    private String gstin;

    @Column(name = "pan", length = 15)
    private String pan;

    @Column(name = "billing_address", columnDefinition = "TEXT")
    private String billingAddress;

    @Column(name = "bank_details", columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> bankDetails;

    @Column(name = "document_series", nullable = false, columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> documentSeries;

    @Column(name = "subscription", nullable = false, columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> subscription;

    @Column(name = "invoice_footer_terms", columnDefinition = "TEXT")
    private String invoiceFooterTerms;

    @Column(name = "status", length = 9, columnDefinition = "ENUM('Active','Inactive') DEFAULT 'Active'")
    private String status = "Active";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = java.util.UUID.randomUUID().toString();
        }
        if (this.status == null) {
            this.status = "Active";
        }
        if (this.gstRegistered == null) {
            this.gstRegistered = false;
        }
        if (this.documentSeries == null) {
            this.documentSeries = new HashMap<>();
        }
        if (this.subscription == null) {
            this.subscription = new HashMap<>();
        }
    }
}
