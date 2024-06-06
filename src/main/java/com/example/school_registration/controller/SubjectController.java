package com.example.school_registration.controller;


import com.example.school_registration.dto.CourseDto;
import com.example.school_registration.dto.DepartmentDto;
import com.example.school_registration.dto.SubjectDto;
import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Department;
import com.example.school_registration.models.Subjects;
import com.example.school_registration.service.CoursesService;
import com.example.school_registration.service.DepartmentService;
import com.example.school_registration.service.SubjectService;
import com.example.school_registration.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;
    @PostMapping("/new")
    public ResponseEntity<?> createCourse(@RequestBody SubjectDto subjectDto){
        Response<Subjects>  response =  subjectService.createSubject(subjectDto);

        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllSubjects(@RequestParam(value = "page", defaultValue = "0")Integer page,
                                           @RequestParam(value = "size",defaultValue = "25")Integer size){
        PageRequest pageRequest =  PageRequest.of(page,size);

        Page<Subjects> subjectsPage =  subjectService.getAllSubjects(pageRequest);

        return ResponseEntity.ok().body(subjectsPage);
    }
    @GetMapping("get/{uuid}")
    public ResponseEntity<?>  getSubjectByID(@PathVariable String uuid){

        Response<Subjects> response =  subjectService.findSubjectByUuid(uuid);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteSubject(@PathVariable String uuid){
        Response<Subjects> response =  subjectService.deleteSubject(uuid);

        return ResponseEntity.ok().body(response);
    }
}
