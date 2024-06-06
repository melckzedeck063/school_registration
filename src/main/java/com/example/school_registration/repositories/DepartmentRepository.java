package com.example.school_registration.repositories;

import com.example.school_registration.models.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Long> {

    Optional<Department> findFirstByUuid(String uuid);

    Optional<Department> findFirstByDepartment(String department);

    Page<Department> findAllByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);


}
