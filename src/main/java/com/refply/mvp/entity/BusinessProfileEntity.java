package com.refply.mvp.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "business_profiles")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BusinessProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String businessName;
    @Column(nullable = false)
    private String businessAddress;
    @Column(nullable = false)
    private String businessPhone;
    @Column(nullable = false)
    private String businessEmail;
    @Column(nullable = false)
    private String pincode;
    private String city;
    private String state;
    private String businessDescription;

    @Column(name = "no_of_programs")
    private Integer noOfProgram = 0;
    @Column(name = "active_programs")
    private Integer  noOfRunningProram = 0;

    @OneToMany(mappedBy = "businessProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionTable(name = "business_referel_programs", joinColumns = @JoinColumn(name = "business_id"))
    private List<ReferelProgramEntity> referelPrograms;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean isWalletEnebled = false;

    private Double walletBalance = 0.0;

}
