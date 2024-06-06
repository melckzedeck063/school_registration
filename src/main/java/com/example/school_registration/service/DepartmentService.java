package com.example.school_registration.service;

import com.example.school_registration.dto.DepartmentDto;
import com.example.school_registration.models.Department;
import com.example.school_registration.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

    Response<Department> createDepartment(DepartmentDto departmentDto);
    Response<Department>  findDeptByUuid(String uuid);

    Response<Department> findDeptByName(String name);

    Page<Department> getAllDepartments(Pageable pageable);

    Response<Department> deleteDepartment(String uuid);


}
