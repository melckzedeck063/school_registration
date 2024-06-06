package com.example.school_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubjectDto {
    private String subjectName;
    private String subjectCode;
    private String courseUuid;
}
