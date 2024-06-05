package com.example.school_registration;

import com.example.school_registration.models.UserAccount;
import com.example.school_registration.repositories.UserAccountRepository;
import com.example.school_registration.utils.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Component // Mark as Spring component to be automatically scanned and initialized
public class Initializer implements ApplicationRunner {

    @PersistenceContext
    private EntityManager entityManager;
    private Logger logger = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @Autowired // Constructor injection
    public Initializer(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        UserAccount userAccount;

        Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername("admin@school.co.tz");

        if (optionalUserAccount.isEmpty()) {
            userAccount = new UserAccount();

            logger.info("=============  CREATING DEFAULT USER ================");

            userAccount.setUsername("admin@school.co.tz");
            userAccount.setFirstName("Super");
            userAccount.setLastName("Admin");
            userAccount.setUserType(String.valueOf(UserType.SUPER_ADMIN));
            userAccount.setPhone("255780080080");
            userAccount.setPassword(passwordEncoder.encode("school123")); // Call encode() on the injected PasswordEncoder
            userAccount.setEnabled(true);

            userAccountRepository.save(userAccount);
            logger.info("============= DEFAULT USER CREATED SUCCESSFUL ================");
        }
    }
}
