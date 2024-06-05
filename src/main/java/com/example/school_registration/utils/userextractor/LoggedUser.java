package com.example.school_registration.utils.userextractor;

import com.example.school_registration.models.UserAccount;

public interface LoggedUser {

    UserInfo getInfo();

    UserAccount getUser();
}
