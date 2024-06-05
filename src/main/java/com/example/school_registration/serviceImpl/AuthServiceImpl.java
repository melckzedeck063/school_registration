package com.example.school_registration.serviceImpl;

import com.example.school_registration.dto.LoginDto;
import com.example.school_registration.dto.LoginResponseDto;
import com.example.school_registration.jwt.JWTUtils;
import com.example.school_registration.models.UserAccount;
import com.example.school_registration.dto.UserAccountDto;
import com.example.school_registration.repositories.UserAccountRepository;
import com.example.school_registration.service.AuthService;
import com.example.school_registration.utils.Response;
import com.example.school_registration.utils.ResponseCode;
import com.example.school_registration.utils.UserType;
import com.example.school_registration.utils.userextractor.LoggedUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserAccountRepository accountRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private JWTUtils jwtUtils;



    @Override
    public Response<LoginResponseDto> login(LoginDto loginDto) {
        try {
            log.info("LOGIN CREDENTIALS : " , loginDto);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtUtils.generateJwtToken(authentication);
            String refreshToken = UUID.randomUUID().toString();

            Optional<UserAccount> accountOptional = accountRepository.findFirstByUsername(authentication.getName());

            return getLoginResponseResponse(accountOptional, jwtToken, refreshToken);


        }catch (Exception e){
            e.printStackTrace();
            return new Response<>(true, ResponseCode.FAIL, e.getMessage());
        }
    }

    @Override
    public Response<UserAccount> registerUser(UserAccountDto userAccountDto) {
        try {

            if (!isValidEmail(userAccountDto.getUsername()))
                return new Response<>(true, ResponseCode.INVALID_REQUEST, null, "Please enter a valid email");

            if (!isValidPhoneNumber(userAccountDto.getPhoneNumber()))
                return new Response<>(true, ResponseCode.INVALID_REQUEST, null, "Please enter a valid phone number");

            Optional<UserAccount> accountOptional = accountRepository.findFirstByPhone(userAccountDto.getPhoneNumber());
            if (accountOptional.isPresent())
                return new Response<>(true, ResponseCode.DUPLICATE, null, "Duplicate, phone number already in use");

            Optional<UserAccount> firstByUsername = accountRepository.findFirstByUsername(userAccountDto.getUsername());
            if (firstByUsername.isPresent())
                return new Response<>(true, ResponseCode.DUPLICATE_EMAIL, "Duplicate, email already in use");

            Random random = new SecureRandom();
            int nextInt = random.nextInt(100001, 999999);
            UserAccount account = new UserAccount();

            if(userAccountDto.getUserRole() == "" || userAccountDto.getUserRole() == null ){
                account.setUserType(UserType.CUSTOMER.toString());
            }
            else {
                account.setUserType(userAccountDto.getUserRole());
            }

            if(userAccountDto.getNationality() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT, "Nationality can not be null");
            }
            if(userAccountDto.getMiddleName() != null){
                account.setNationality(userAccountDto.getMiddleName());
            }
            else  {
                account.setMiddleName("");
            }

            account.setEnabled(false);
            account.setPhone(userAccountDto.getPhoneNumber());
            account.setUsername(userAccountDto.getUsername());
            account.setFirstName(userAccountDto.getFirstName());
            account.setLastName(userAccountDto.getLastName());
            account.setNationality(userAccountDto.getNationality());
            account.setPassword(passwordEncoder.encode(userAccountDto.getPassword().trim()));

//            MessageRequestDto messageRequestDto = new MessageRequestDto();
//            messageRequestDto.setEncoding(0);
//            messageRequestDto.setMessage("Your verification code is "+nextInt+", use this to activate your account");
//            List<Recipient> recipients = new ArrayList<>();
//            recipients.add(new Recipient(1, registerDto.getPhoneNumber().startsWith("+") ? registerDto.getNidaNumber().trim() : "+"+registerDto.getPhoneNumber()));
//            messageRequestDto.setRecipients(recipients);
//            messageRequestDto.setSource_addr("INFO");
//
//            Response<ResponseMessage> responseMessageResponse = bulkSmsIntegrationService.sendMessage(messageRequestDto);
//
//            log.info("SEND SMS INFO {}", responseMessageResponse);

                account.setLastOtpSentAt(LocalDateTime.now());
                accountRepository.save(account);
                return new Response<>(false, ResponseCode.SUCCESS, account, "Account registered successfully");

        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL, null, "Failed to register account unknown error occurred");
    }

    @Override
    public Response<LoginResponseDto> revokeToken(String refreshToken) {
        try {

            Optional<UserAccount> accountOptional = accountRepository.findFirstByRefreshToken(refreshToken);
            if (accountOptional.isEmpty()){
                return new Response<>(true, ResponseCode.FAIL, null, null, "Invalid refresh token");
            }


            return getLoginResponseResponse(accountOptional, "", "");

        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL, null, null, "Failed to refresh token");
    }

    @Override
    public Response<Boolean> forgetPassword(String email) {
        return null;
    }

    @Override
    public Response<UserAccount> getLoggedUser() {
        try {
            UserAccount user = loggedUser.getUser();
            if (user == null)
                return new Response<>(true, ResponseCode.UNAUTHORIZED, "Unauthorized");
            return new Response<>(false, ResponseCode.SUCCESS, user, "Success");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL, "Operation Unsuccessful");
    }

//    @Override
//    public Response<String> activateAccountThroughOTP(String otp) {
//        try {
//            Optional<UserAccount> firstByOneTimePassword = accountRepository.findFirstByOneTimePassword(otp);
//            if (firstByOneTimePassword.isEmpty())
//                return new Response<>(true, ResponseCode.USER_NOT_FOUND, "Invalid OTP, could not find account associated with this code");
//            UserAccount account = firstByOneTimePassword.get();
//            account.setEnabled(true);
//            accountRepository.save(account);
//            return new Response<>(false, ResponseCode.SUCCESS, "Your account has been activated, please login to continue");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new Response<>(true, ResponseCode.FAIL, "Failed to activate your account");
//    }

//    @Override
//    public Response<String> requestNewOtp(String phone) {
//        try {
//            Optional<UserAccount> accountOptional = accountRepository.findFirstByPhone(phone);
//            if (accountOptional.isEmpty())
//                return new Response<>(true, ResponseCode.USER_NOT_FOUND, "Invalid OTP, could not find account associated with this code");
//
//            Random random = new SecureRandom();
//            int nextInt = random.nextInt(100001, 999999);
//
//            UserAccount account = accountOptional.get();
//            account.setOneTimePassword(String.valueOf(nextInt));
//
//            accountRepository.save(account);
//
//            return new Response<>(false, ResponseCode.SUCCESS, "Verification code sent to your phone number");
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new Response<>(true, ResponseCode.FAIL, "Failed to request new code");
//    }


    @NotNull
    private Response<LoginResponseDto> getLoginResponseResponse(Optional<UserAccount> accountOptional, String jwtToken, String refreshToken) {
        if (accountOptional.isPresent()){

            UserAccount account = accountOptional.get();
            account.setRefreshToken(refreshToken);
            account.setRefreshTokenCreatedAt(LocalDateTime.now());
            accountRepository.save(account);
            LoginResponseDto response = new LoginResponseDto(
                    jwtToken,
                    refreshToken,
                    "Bearer",
                    account.getUsername(),
                    account.getUserType(),
                    account.getFirstName()
            );

            return new Response<>(false, ResponseCode.SUCCESS, response, null, "Login successful");

        }

        return new Response<>(true, ResponseCode.FAIL, "Failed to login");
    }

    private boolean isValidEmail(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private boolean isValidPhoneNumber(String number) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("(^(([2]{1}[5]{2})|([0]{1}))[1-9]{2}[0-9]{7}$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(number);
        return matcher.find();
    }

    private HttpServletRequest getAuthorizationHeader() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

}
