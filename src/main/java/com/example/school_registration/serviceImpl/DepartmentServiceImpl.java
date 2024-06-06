package com.example.school_registration.serviceImpl;

import com.example.school_registration.dto.DepartmentDto;
import com.example.school_registration.models.Department;
import com.example.school_registration.models.UserAccount;
import com.example.school_registration.repositories.DepartmentRepository;
import com.example.school_registration.repositories.UserAccountRepository;
import com.example.school_registration.service.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public Response<Department> createDepartment(DepartmentDto departmentDto) {
        try {
            UserAccount user  = loggedUser.getUser();

            Department department =  new Department();

            if(user == null) {
                return new Response<>(true, ResponseCode.UNAUTHORIZED,"unauthorized");
            }

            if(departmentDto.getDepartment() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"department name can nt be null");
            }

            if(departmentDto.getDeptCode() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"department code can nt be null");
            }

            if(departmentDto.getHodUuid() != null){
                Optional<UserAccount> userAccountOptional = userAccountRepository.findFirstByUuid(departmentDto.getHodUuid());

                userAccountOptional.ifPresent(department::setHod);

                if(userAccountOptional.isEmpty()){
                    return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that ID");
                }
            }

            if(!departmentDto.getDepartment().isBlank() && !Objects.equals(departmentDto.getDepartment(), department.getDepartment())){
                department.setDepartment(departmentDto.getDepartment());
            }

            if(!departmentDto.getDeptCode().isBlank() && !Objects.equals(departmentDto.getDeptCode(), department.getDeptCode())){
                department.setDeptCode(departmentDto.getDeptCode());
            }

            department.setRegisteredBy(user);

            Department department1 =  departmentRepository.save(department);

            return new Response<>(false,ResponseCode.SUCCESS,department1,"New department added");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Department> findDeptByUuid(String uuid) {
        try {
            Optional<Department> optionalDepartment =  departmentRepository.findFirstByUuid(uuid);

            if(optionalDepartment.isEmpty())
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that ID");

            return new Response<>(false,ResponseCode.SUCCESS,optionalDepartment.get(),"Data found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Department> findDeptByName(String name) {
        try {
            Optional<Department> optionalDepartment =  departmentRepository.findFirstByDepartment(name);

            if(optionalDepartment.isEmpty())
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that name");

            return new Response<>(false,ResponseCode.SUCCESS,optionalDepartment.get(),"Data found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Page<Department> getAllDepartments(Pageable pageable) {
        try {
            Page<Department>  departmentPage =  departmentRepository.findAllByDeletedFalseOrderByCreatedAtDesc(pageable);
            return  departmentPage;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response<Department> deleteDepartment(String uuid) {
        try {
            Optional<Department> optionalDepartment =  departmentRepository.findFirstByUuid(uuid);

            if(optionalDepartment.isEmpty())
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that ID");

            Department department =  optionalDepartment.get();
            departmentRepository.delete(department);

            return new Response<>(false,ResponseCode.SUCCESS,"Department deleted successful");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }


}
