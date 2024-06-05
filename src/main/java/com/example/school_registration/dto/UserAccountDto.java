package com.example.school_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {

    private String uuid;
    private String username;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String userRole;
    private String password;
    private String nationality;

}

