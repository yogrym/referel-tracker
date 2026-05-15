package com.grymprojects.openbeta.model;

import com.grymprojects.openbeta.enums.BusinessRole;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "business_onboarding")
public class BusinessOnboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_email", nullable = false, unique = true)
    private String businessEmail;

    @Column(name = "business_address")
    private String businessAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "web_address")
    private String webAddress;

    @Column(name = "gst_number", unique = true)
    private String gstNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_role", nullable = false)
    private BusinessRole businessRole;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}