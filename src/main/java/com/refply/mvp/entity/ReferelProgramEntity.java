package com.refply.mvp.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "referel_program")
public class ReferelProgramEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "program_type")
    private String programType;
    
  
    @Column(name = "refer_program_url", nullable = true, unique = true)
    private String url;


    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String type;

    @Column(name = "min_bill_quantity")
    private Integer minBillQuantity;

    @Column(name = "commision_type", nullable = false)
    private String commisionType;

    @Column(name = "commision_value", nullable = false)
    private String commisionValue;

    @Column(name = "firends_commision", nullable = false)
    private String firendsCommision;

    @ElementCollection
    @CollectionTable(name = "program_free_services", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "service_name")
    private List<String> freeService;

    @Column(name = "max_referel", nullable = false)
    private String maxReferel;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_profile_id")
    @JsonIgnore
    private BusinessProfileEntity businessProfile;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
