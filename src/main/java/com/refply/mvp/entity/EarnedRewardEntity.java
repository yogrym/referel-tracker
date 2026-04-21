package com.refply.mvp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "earned_rewards")
public class EarnedRewardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joined_program_id", nullable = false)
    private JoinedProramEntity joinedProgram;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", nullable = false)
    private RewardType rewardType;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "percentage_value", precision = 5, scale = 2)
    private BigDecimal percentageValue;

    @Column(name = "credit_amount", precision = 12, scale = 2)
    private BigDecimal creditAmount;

    @Column(nullable = false)
    @Builder.Default
    private Boolean redeemed = Boolean.FALSE;

    private LocalDateTime redeemedAt;

    @CreationTimestamp
    @Column(name = "earned_at", nullable = false, updatable = false)
    private LocalDateTime earnedAt;

    @PrePersist
    @PreUpdate
    private void validateRewardPayload() {
        if (rewardType == null) {
            throw new IllegalStateException("rewardType is required");
        }

        switch (rewardType) {
            case FREE_SERVICE -> {
                requireText(serviceName, "serviceName");
                percentageValue = null;
                creditAmount = null;
            }
            case PERCENT_DISCOUNT -> {
                requirePositive(percentageValue, "percentageValue");
                serviceName = null;
                creditAmount = null;
            }
            case STORE_CREDIT -> {
                requirePositive(creditAmount, "creditAmount");
                serviceName = null;
                percentageValue = null;
            }
        }
    }

    private void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(fieldName + " is required for " + rewardType);
        }
    }

    private void requirePositive(BigDecimal value, String fieldName) {
        if (value == null || value.signum() <= 0) {
            throw new IllegalStateException(fieldName + " must be positive for " + rewardType);
        }
    }
}
