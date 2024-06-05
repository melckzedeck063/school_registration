package com.example.school_registration.serviceImpl;

import com.example.school_registration.models.UserAccount;
import com.example.school_registration.dto.UserAccountDto;
import com.example.school_registration.repositories.UserAccountRepository;
import com.example.school_registration.service.UserAccountService;
import com.example.school_registration.utils.userextractor.LoggedUser;
import com.example.school_registration.utils.Response;
import com.example.school_registration.utils.ResponseCode;
import com.example.school_registration.utils.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    @Autowired
    private UserAccountRepository userAccountRepository;
    private UserAccount userAccount;

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Response<UserAccount> createUpdateUser(UserAccountDto userAccountDto) {
        try {
            UserAccount user =  loggedUser.getUser();

            if(user  == null){
                logger.info("UNAUTHORIZED USER TRYING TO CREATE USER");
                return new Response<>(true, ResponseCode.UNAUTHORIZED, "Full authentication is required");
            }

            Optional<UserAccount> accountOptional =  userAccountRepository.findFirstByUsername(userAccountDto.getUsername());
            Optional<UserAccount> accountOptional2 =  userAccountRepository.findFirstByPhone(userAccountDto.getPhoneNumber());
            if(accountOptional.isPresent()){
                return new Response<>(true,ResponseCode.DUPLICATE_EMAIL,"Username arleady exist");
            }

            if(accountOptional2.isPresent()){
                return new Response<>(true,ResponseCode.DUPLICATE,"Phone number already exist");
            }

            UserAccount userAccount1 =  new UserAccount();

            if(userAccountDto.getFirstName() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT, "Firstname can not be null");
            }

            if(userAccountDto.getLastName() == null){
                return new Response<>(true, ResponseCode.NULL_ARGUMENT, "Lastname can not be empty");
            }

            if(userAccountDto.getUsername() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"Username can not be empty");
            }

            if(userAccountDto.getPhoneNumber() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT, "Phone number can not be empty");
            }

            if(userAccountDto.getNationality() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT, "Nationality can not be null");
            }

            if(userAccountDto.getMiddleName() == null){
                userAccount1.setMiddleName("");
            }
            else {
                if(!userAccountDto.getMiddleName().isBlank() && !Objects.equals(userAccountDto.getMiddleName(), userAccount1.getMiddleName()))
                    userAccount1.setMiddleName(userAccountDto.getMiddleName());
            }

            if(!userAccountDto.getFirstName().isBlank() && !Objects.equals(userAccountDto.getFirstName(),userAccount1.getFirstName()))
                userAccount1.setFirstName(userAccountDto.getFirstName());

            if(!userAccountDto.getLastName().isBlank() && !Objects.equals(userAccountDto.getLastName(), userAccount1.getLastName()))
                userAccount1.setLastName(userAccountDto.getLastName());


            if(!userAccountDto.getUsername().isBlank() && !Objects.equals(userAccountDto.getUsername(), userAccount1.getUsername()))
                userAccount1.setUsername(userAccountDto.getUsername());

            if(!userAccountDto.getPhoneNumber().isBlank() && !Objects.equals(userAccountDto.getPhoneNumber(), userAccount1.getPhone()))
                userAccount1.setPhone(userAccountDto.getPhoneNumber());

            if(!userAccountDto.getNationality().isBlank() && !Objects.equals(userAccountDto.getNationality(), userAccount1.getNationality()))
                userAccount1.setNationality(userAccountDto.getNationality());


            if(userAccountDto.getUserRole() == null){
                userAccount1.setUserType(String.valueOf(UserType.CUSTOMER));
            }
            else if(userAccountDto.getUserRole().equalsIgnoreCase(UserType.ADMIN.name()))
                userAccount1.setUserType(String.valueOf(UserType.ADMIN));
            else if (userAccountDto.getUserRole().equalsIgnoreCase(UserType.SUPER_ADMIN.name()))
                userAccount1.setUserType(String.valueOf(UserType.SUPER_ADMIN));
            else if (userAccountDto.getUserRole().equalsIgnoreCase(UserType.SELLER.name()))
                userAccount1.setUserType(String.valueOf(UserType.SELLER));
            else if(userAccountDto.getUserRole().equalsIgnoreCase(UserType.VENDOR.name()))
                userAccount1.setUserType(String.valueOf(UserType.VENDOR));
            else userAccount1.setUserType(String.valueOf(UserType.CUSTOMER));

            if(userAccount1.getPassword() == null){
                userAccount1.setPassword(passwordEncoder.encode(userAccountDto.getLastName().toUpperCase().trim()));
            }

            UserAccount userAccount2 =  userAccountRepository.save(userAccount1);

            return new Response<>(false, ResponseCode.SUCCESS,userAccount2,"User created successfully");

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return new Response<>(true,ResponseCode.FAIL,"Failed to create user");
    }

    @Override
    public Response<UserAccount> deleteUserAccount(String uuid) {
        try {
             UserAccount user = loggedUser.getUser();

             if(user == null){
                 logger.info("UNAUTHORIZED USER TRYING TO DELETE USER");
                 return new Response<>(true,ResponseCode.UNAUTHORIZED, "Full authentication ir required");
             }

             Optional<UserAccount> accountOptional = userAccountRepository.findFirstByUuid(uuid);

             if(accountOptional.isEmpty())
                 return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found");

             userAccountRepository.delete(accountOptional.get());

             return new Response<>(false, ResponseCode.SUCCESS,"User deleted successfully");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Failed to create user");
    }

    @Override
    public Response<UserAccount> getUserByUuid(String uuid) {
        try {
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUuid(uuid);
            if(optionalUserAccount.isPresent()) {
                return new Response<>(false, ResponseCode.SUCCESS, optionalUserAccount.get(), "User found");
            }
            else {
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"User not  found");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(false,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Page<UserAccount> getCustomers(Pageable pageable) {
        try {
             return userAccountRepository.findAllByUserType(String.valueOf(UserType.CUSTOMER),pageable);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return new PageImpl<>(new ArrayList<>());
    }

    @Override
    public Page<UserAccount> getOfficials(Pageable pageable) {
       try{
           Page<UserAccount> accountPage = userAccountRepository.findAllByUserTypeNot(String.valueOf(UserType.CUSTOMER), pageable);
           return accountPage;
       }
       catch (Exception e){
           e.printStackTrace();
           return new PageImpl<>(new ArrayList<>());
       }

    }


}
