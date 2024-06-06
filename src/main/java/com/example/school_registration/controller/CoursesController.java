package com.example.school_registration.controller;


import com.example.school_registration.dto.CourseDto;
import com.example.school_registration.dto.DepartmentDto;
import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Department;
import com.example.school_registration.service.CoursesService;
import com.example.school_registration.service.DepartmentService;
import com.example.school_registration.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/courses")
public class CoursesController {

    @Autowired
    private CoursesService coursesService;
    @PostMapping("/new")
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto){
        Response<Courses>  response =  coursesService.createCourse(courseDto);

        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllCourses(@RequestParam(value = "page", defaultValue = "0")Integer page,
                                        @RequestParam(value = "size",defaultValue = "25")Integer size){
        PageRequest pageRequest =  PageRequest.of(page,size);

        Page<Courses> coursesPage =  coursesService.getAllCourses(pageRequest);

        return ResponseEntity.ok().body(coursesPage);
    }
    @GetMapping("get/{uuid}")
    public ResponseEntity<?>  getCourseByID(@PathVariable String uuid){

        Response<Courses> response =  coursesService.findCourseByUuid(uuid);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteCourse(@PathVariable String uuid){
        Response<Courses> response =  coursesService.deleteCourse(uuid);

        return ResponseEntity.ok().body(response);
    }
}
