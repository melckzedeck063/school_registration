package com.example.school_registration.service;

import com.example.school_registration.models.UserAccount;
import com.example.school_registration.dto.UserAccountDto;
import com.example.school_registration.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAccountService {
    Response<UserAccount> createUpdateUser(UserAccountDto userAccountDto);

    Response<UserAccount> deleteUserAccount(String uuid);

    Response<UserAccount> getUserByUuid(String uuid);

    Page<UserAccount> getCustomers(Pageable pageable);

    Page<UserAccount>  getOfficials (Pageable pageable);
}
