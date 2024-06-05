package com.example.school_registration.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Basic(optional = true)
    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Where(clause = "deleted = false")
    @Column(columnDefinition = "boolean default false")
    private Boolean deleted = false;

    @Column(columnDefinition = "boolean default true")
    @Where(clause = "active = true")
    private Boolean active = true;

    @Column(unique = true)
    private String uuid = UUID.randomUUID().toString();
}

