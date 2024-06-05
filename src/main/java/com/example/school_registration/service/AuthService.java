package com.example.school_registration.service;

import com.example.school_registration.dto.LoginDto;
import com.example.school_registration.dto.LoginResponseDto;
import com.example.school_registration.dto.UserAccountDto;
import com.example.school_registration.models.UserAccount;
import com.example.school_registration.utils.Response;

public interface AuthService {

    Response<LoginResponseDto> login(LoginDto loginDto);

    Response registerUser(UserAccountDto userAccountDto);
    Response<LoginResponseDto> revokeToken(String refreshToken);
    Response<Boolean> forgetPassword(String email);
    Response<UserAccount> getLoggedUser();


}

