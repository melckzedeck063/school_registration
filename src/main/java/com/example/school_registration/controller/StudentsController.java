package com.example.school_registration.controller;

import com.example.school_registration.dto.StudentDto;
import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Students;
import com.example.school_registration.service.StudentsService;
import com.example.school_registration.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
public class StudentsController {

    @Autowired
    private StudentsService studentsService;

    @PostMapping("/add")
    public ResponseEntity<?> registerStudent(@RequestBody StudentDto studentDto){
        Response<Students>  response =  studentsService.createStudent(studentDto);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllStudents(@RequestParam(value = "page", defaultValue = "0")Integer page,
                                            @RequestParam(value = "size", defaultValue = "25")Integer size){
        PageRequest pageRequest =  PageRequest.of(page,size);

        Page<Students>  studentsPage =  studentsService.getAllStudents(pageRequest);

        return ResponseEntity.ok().body(studentsPage);
    }

    @GetMapping("/by-course")
    public ResponseEntity<?> getStudentsByCourse(@PathVariable Courses courses, @RequestParam(value = "page", defaultValue = "0")Integer page,
                                                 @RequestParam(value = "size", defaultValue = "25")Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Students> studentsPage = studentsService.getStudentByCourse(courses, pageRequest);

        return ResponseEntity.ok().body(studentsPage);
    }

    @GetMapping("/get/{uuid}")
    public ResponseEntity<?> getStudentById(@PathVariable String uuid){
        Response<Students> response =  studentsService.getStudentByUuid(uuid);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteStudent(@PathVariable String uuid){
        Response<Students> response =   studentsService.deleteStudent(uuid);

        return ResponseEntity.ok().body(response);
    }


}
