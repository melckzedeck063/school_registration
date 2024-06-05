package com.example.school_registration.service;

import com.example.school_registration.dto.SchoolsDto;
import com.example.school_registration.models.Schools;
import com.example.school_registration.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolsService {

    Response<Schools>  registerSchool(SchoolsDto schoolsDto);

    Response<Schools> findSchoolByUuid(String uuid);

    Page<Schools> getAllSchools(Pageable pageable);

    Response<Schools> deleteSchool(String uuid);
}
