package com.example.school_registration.repositories;

import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Subjects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subjects, Long> {
    Optional<Subjects> findFirstByUuid(String uuid);

    Optional<Subjects> findFirstBySubjectName(String name);

    Optional<Subjects> findFirstBySubjectCode(String code);

    Page<Subjects> findAllByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
}
