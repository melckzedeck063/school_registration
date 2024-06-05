package com.example.school_registration.userDetailService;


import com.example.school_registration.models.UserAccount;
import com.example.school_registration.repositories.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = repository.findFirstByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return UserDetailsImpl.build(userAccount);
    }
}
