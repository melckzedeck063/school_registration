package com.example.school_registration.service;

import com.example.school_registration.dto.CourseDto;
import com.example.school_registration.dto.DepartmentDto;
import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Department;
import com.example.school_registration.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CoursesService {

    Response<Courses> createCourse(CourseDto courseDto);
    Response<Courses>  findCourseByUuid(String uuid);

    Response<Courses> findCourseByName(String name);

    Page<Courses> getAllCourses(Pageable pageable);

    Response<Courses> deleteCourse(String uuid);
}
