package com.example.school_registration.repositories;

import com.example.school_registration.models.Schools;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<Schools,Long> {

    Optional<Schools> findFirstByUuid(String  uuid);

    Optional<Schools> findFirstBySchool(String school);

    Page<Schools> findAllByDeletedFalseOrderByCreatedAtDesc(Pageable  pageable);
}
