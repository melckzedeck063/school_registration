package com.example.school_registration.repositories;

import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Students;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentsRepository extends JpaRepository<Students,Long> {
    Optional<Students> findFirstByUuid(String uuid);

    Optional<Students> findFirstByFirstnameAndLastnameAndDeletedFalse(String firstname,String lastname);

    Page<Students> findAllByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<Students> findAllByCourseIsAndDeletedFalse(Courses course, Pageable pageable);

}
