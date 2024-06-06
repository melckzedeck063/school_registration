package com.example.school_registration.service;

import com.example.school_registration.dto.StudentDto;
import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Students;
import com.example.school_registration.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;

public interface StudentsService {
    Response<Students> createStudent(@RequestBody StudentDto studentDto);

    Response<Students>  getStudentByUuid(String uuid);

    Response<Students> getStudentByName(String fname,String lname);

    Page<Students>  getAllStudents(Pageable pageable);

    Response<Students> deleteStudent(String uuid);

    Page<Students> getStudentByCourse(Courses course, Pageable pageable);
}
