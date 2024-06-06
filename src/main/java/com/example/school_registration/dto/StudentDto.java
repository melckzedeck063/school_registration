package com.example.school_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDto {
    private String firstname;
    private String lastname;
    private String gender;
    private int age;
    private String courseUuid;
}
