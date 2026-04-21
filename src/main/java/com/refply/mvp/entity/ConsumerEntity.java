package com.refply.mvp.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Collate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumers")
public class ConsumerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false, name = "mobile_nummber")
    private int phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

   @CreationTimestamp
   @Column(name = "created_time", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updatedAt;

    @ElementCollection
    @Column(name = "joined_programs")
    private List<JoinedProramEntity> joinedPrograms;
    
}
