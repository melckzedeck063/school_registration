package com.example.school_registration.controller;


import com.example.school_registration.dto.SchoolsDto;
import com.example.school_registration.models.Schools;
import com.example.school_registration.service.SchoolsService;
import com.example.school_registration.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/school")
public class SchoolsController {

    @Autowired
    private SchoolsService schoolsService;

    @PostMapping("/new")
    public ResponseEntity<?> registerSchool(@RequestBody SchoolsDto schoolsDto){
        Response<Schools> response =  schoolsService.registerSchool(schoolsDto);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllSchools(@RequestParam(value = "page", defaultValue = "0")Integer page,
                                           @RequestParam(value = "size", defaultValue = "25")Integer size){
        PageRequest pageRequest =  PageRequest.of(page,size);

        Page<Schools> schoolsPage =  schoolsService.getAllSchools(pageRequest);

        return ResponseEntity.ok().body(schoolsPage);

    }

    @GetMapping("/get/{uuid}")
    public ResponseEntity<?> getSchoolByUuid(@PathVariable String uuid){
        Response<Schools> response =  schoolsService.findSchoolByUuid(uuid);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteSchool(@PathVariable String uuid){
        Response<Schools> response =  schoolsService.deleteSchool(uuid);

        return ResponseEntity.ok().body(response);
    }

}
