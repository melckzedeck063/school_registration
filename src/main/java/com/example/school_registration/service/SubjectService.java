package com.example.school_registration.service;

import com.example.school_registration.dto.CourseDto;
import com.example.school_registration.dto.SubjectDto;
import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Subjects;
import com.example.school_registration.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SubjectService {

    Response<Subjects> createSubject(SubjectDto subjectDto);
    Response<Subjects>  findSubjectByUuid(String uuid);

    Response<Subjects> findSubjectByName(String name);

    Page<Subjects> getAllSubjects(Pageable pageable);

    Response<Subjects> deleteSubject(String uuid);
}
