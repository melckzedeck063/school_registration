package com.example.school_registration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private Logger logger = LoggerFactory.getLogger(AuthenticationSuccessListener.class.getSimpleName());

    @Autowired
    private com.example.school_registration.config.BruteForceProtectionService bruteForceProtectionService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        logger.info("LOGIN SUCCESSFUL FOR USER ====================> {}", username);
        bruteForceProtectionService.resetBruteForceCounter(username);
    }
}
