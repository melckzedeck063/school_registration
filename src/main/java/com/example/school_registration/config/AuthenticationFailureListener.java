package com.example.school_registration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private Logger logger = LoggerFactory.getLogger(com.example.school_registration.config.AuthenticationSuccessListener.class);

    @Autowired
    private com.example.school_registration.config.BruteForceProtectionService bruteForceProtectionService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        logger.info("LOGIN FAILED FOR USER ====================> {}", username);
        bruteForceProtectionService.registerLoginFailure(username);
    }

}
