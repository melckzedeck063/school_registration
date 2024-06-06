package com.example.school_registration.repositories;

import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoursesRepository extends JpaRepository<Courses,Long> {
    Optional<Courses> findFirstByUuid(String uuid);

    Optional<Courses> findFirstByCourseName(String course);

    Page<Courses> findAllByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
}
