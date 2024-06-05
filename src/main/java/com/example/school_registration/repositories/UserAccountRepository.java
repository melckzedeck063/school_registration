package com.example.school_registration.repositories;

import com.example.school_registration.models.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findFirstByUsername(String username);

    Optional<UserAccount> findFirstByPhone(String phone);

    Optional<UserAccount> findFirstByUuid(String uuid);

    Optional<UserAccount> findFirstByRefreshToken(String token);

    Page<UserAccount> findAllByUserType(String role, Pageable pageable);

    Page<UserAccount> findAllByUserTypeNot(String role, Pageable pageable);

    Long countAllByUserType(String userType);

    Long countAllByUserTypeNot(String userType);

}
