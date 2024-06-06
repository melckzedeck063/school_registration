package com.example.school_registration.controller;


import com.example.school_registration.dto.DepartmentDto;
import com.example.school_registration.models.Department;
import com.example.school_registration.service.DepartmentService;
import com.example.school_registration.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/new")
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto departmentDto){
        Response<Department>  response =  departmentService.createDepartment(departmentDto);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDept(@RequestParam(value = "page", defaultValue = "0")Integer page,
                                        @RequestParam(value = "size",defaultValue = "25")Integer size){
        PageRequest pageRequest =  PageRequest.of(page,size);

        Page<Department> departmentPage =  departmentService.getAllDepartments(pageRequest);

        return ResponseEntity.ok().body(departmentPage);
    }

    @GetMapping("get/{uuid}")
    public ResponseEntity<?>  getDepartmentByID(@PathVariable String uuid){

            Response<Department> response =  departmentService.findDeptByUuid(uuid);

            return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteDepartment(@PathVariable String uuid){
        Response<Department> response =  departmentService.deleteDepartment(uuid);

        return ResponseEntity.ok().body(response);
    }
}
