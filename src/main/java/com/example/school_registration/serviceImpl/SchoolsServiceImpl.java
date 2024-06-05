package com.example.school_registration.serviceImpl;
import com.example.school_registration.dto.SchoolsDto;
import com.example.school_registration.models.Schools;
import com.example.school_registration.models.UserAccount;
import com.example.school_registration.repositories.SchoolRepository;
import com.example.school_registration.service.SchoolsService;
import com.example.school_registration.utils.Response;
import com.example.school_registration.utils.ResponseCode;
import com.example.school_registration.utils.userextractor.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SchoolsServiceImpl implements SchoolsService {

    @Autowired
    private SchoolRepository  schoolRepository;

    @Autowired
    private LoggedUser  loggedUser;


    @Override
    public Response<Schools> registerSchool(SchoolsDto schoolsDto) {
        try {
            UserAccount user =  loggedUser.getUser();

            Schools schools =  new Schools();

            if(user == null){
                return new Response<>(true, ResponseCode.UNAUTHORIZED,"Unauthorized");
            }

            if(schoolsDto.getSchool() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"School name can not be null");
            }
            Optional<Schools>  schoolsOptional = schoolRepository.findFirstBySchool(schoolsDto.getSchool());

            if(schoolsOptional.isPresent()){
                return new Response<>(true,ResponseCode.DUPLICATE,"School with that name already exists");
            }

            if(schoolsDto.getDistrict() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"District can not  be null");
            }

            if(schoolsDto.getRegNo() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"School reg no can not be null");
            }

            if(schoolsDto.getRegion() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"region can not be null");
            }

            if(schoolsDto.getStudents()  == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"number of students can not be null");
            }

            if(!schoolsDto.getSchool().isBlank() && !Objects.equals(schoolsDto.getSchool(),schools.getSchool() )){
                schools.setSchool(schoolsDto.getSchool());
            }

            if(!schoolsDto.getRegNo().isBlank() && !Objects.equals(schoolsDto.getRegNo(), schools.getRegNo())){
                schools.setRegNo(schoolsDto.getRegNo());
            }

            if(!schoolsDto.getRegion().isBlank() &&  !Objects.equals(schoolsDto.getRegion(), schools.getRegion())){
                schools.setRegion(schoolsDto.getRegion());
            }

            if(!schoolsDto.getDistrict().isBlank() && !Objects.equals(schoolsDto.getDistrict(), schools.getDistrict())){
                schools.setDistrict(schools.getDistrict());
            }

            schools.setStudents(schoolsDto.getStudents());
            schools.setRegisteredBy(user);

            Schools schools1 =  schoolRepository.save(schools);

            return new Response<>(false,ResponseCode.SUCCESS,schools1,"School registered successful");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Schools> findSchoolByUuid(String uuid) {
        try {
            Optional<Schools>  schoolsOptional =  schoolRepository.findFirstByUuid(uuid);

            if(schoolsOptional.isEmpty()){
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that ID");
            }

            return new Response<>(false,ResponseCode.SUCCESS,schoolsOptional.get(),"Data found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Page<Schools> getAllSchools(Pageable pageable) {
        try {
            Page<Schools>  schools =  schoolRepository.findAllByDeletedFalseOrderByCreatedAtDesc(pageable);

            return schools;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response<Schools> deleteSchool(String uuid) {
        try {
            UserAccount user = loggedUser.getUser();
            if(user == null){
                return new Response<>(true,ResponseCode.UNAUTHORIZED,"Unauthorized");
            }

            Optional<Schools> schoolsOptional = schoolRepository.findFirstByUuid(uuid);

            if(schoolsOptional.isPresent()){
                schoolRepository.delete(schoolsOptional.get());

                return new Response<>(false,ResponseCode.SUCCESS,"School deleted  successful");
            }

            return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that ID");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
