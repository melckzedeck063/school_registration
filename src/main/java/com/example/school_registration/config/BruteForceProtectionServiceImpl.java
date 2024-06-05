package com.example.school_registration.config;

import com.example.school_registration.models.UserAccount;
import com.example.school_registration.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BruteForceProtectionServiceImpl implements com.example.school_registration.config.BruteForceProtectionService {
    //@Value("${tz.go.gmstracker.cache-max-limit}")
    private int maxFailedLogins = 100;

    @Autowired
    UserAccountRepository userRepository;

    //@Value("${tz.go.gmstracker.max-failed-login-count}")
    private int cacheMaxLimit = 3;

    private final ConcurrentHashMap< String, com.example.school_registration.config.FailedLogin> cache;

    public BruteForceProtectionServiceImpl() {
        this.cache = new ConcurrentHashMap < > (cacheMaxLimit); //setting max limit for cache
    }

    @Override
    public void registerLoginFailure(String username) {
        Optional<UserAccount> optionalUserAccount = userRepository.findFirstByUsername(username);
        if (optionalUserAccount.isPresent()){
            UserAccount userAccount = optionalUserAccount.get();
            if (userAccount.isAccountNonLocked()) {
                int loginAttempts = userAccount.getLoginAttempts();
                if (maxFailedLogins < loginAttempts+1){
                    userAccount.setAccountNonLocked(false);
                }else {
                    userAccount.setLoginAttempts(loginAttempts+1);
                }
            }
            userAccount.setLastLoginAttempt(LocalDateTime.now());
            userRepository.save(userAccount);
        }
    }

    @Override
    public void resetBruteForceCounter(String username) {
        Optional<UserAccount> optionalUserAccount = userRepository.findFirstByUsername(username);
        if (optionalUserAccount.isPresent()){
            UserAccount userAccount = optionalUserAccount.get();
            userAccount.setLoginAttempts(0);
            userAccount.setAccountNonLocked(true);
            userRepository.save(userAccount);
        }
    }

    @Override
    public boolean isBruteForceAttack(String username) {
        Optional<UserAccount> optionalUserAccount = userRepository.findFirstByUsername(username);
        if (optionalUserAccount.isPresent()) {
            UserAccount userAccount = optionalUserAccount.get();
            return userAccount.getLoginAttempts() >= maxFailedLogins;
        }
        return false;
    }

    protected com.example.school_registration.config.FailedLogin getFailedLogin(final String username){
        com.example.school_registration.config.FailedLogin failedLogin = cache.get(username.toLowerCase());

        if(failedLogin ==null){
            //setup the initial data
            failedLogin = new com.example.school_registration.config.FailedLogin(0, LocalDateTime.now());
            cache.put(username.toLowerCase(), failedLogin);
            if(cache.size() > cacheMaxLimit){
                cache.remove(username.toLowerCase());
            }
        }
        return failedLogin;
    }

}
