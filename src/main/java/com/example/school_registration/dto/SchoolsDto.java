package com.example.school_registration.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SchoolsDto {
    private String school;

    private String regNo;

    private String region;

    private String district;

    private String students;
}
