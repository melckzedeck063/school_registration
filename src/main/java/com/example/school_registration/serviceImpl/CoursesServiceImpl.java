package com.example.school_registration.serviceImpl;
import com.example.school_registration.dto.CourseDto;
import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Department;
import com.example.school_registration.models.UserAccount;
import com.example.school_registration.repositories.CoursesRepository;
import com.example.school_registration.repositories.DepartmentRepository;
import com.example.school_registration.service.CoursesService;
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
public class CoursesServiceImpl implements CoursesService {

    @Autowired
    private CoursesRepository  coursesRepository;

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private DepartmentRepository departmentRepository;


    @Override
    public Response<Courses> createCourse(CourseDto courseDto) {
        try {
            UserAccount user  = loggedUser.getUser();

            Courses courses =  new Courses();
            if(user == null) {
                return new Response<>(true, ResponseCode.UNAUTHORIZED,"unauthorized");
            }

            if(courseDto.getCourseName() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"course name can nt be null");
            }

            if(courseDto.getCourseCode() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"course code can nt be null");
            }

            if(courseDto.getDepartmentUuid() != null){
                Optional<Department> optionalDepartment = departmentRepository.findFirstByUuid(courseDto.getDepartmentUuid());

                optionalDepartment.ifPresent(courses::setDepartment);

                if(optionalDepartment.isEmpty()){
                    return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that ID");
                }
            }

            if(!courseDto.getCourseName().isBlank() && !Objects.equals(courseDto.getCourseName(), courses.getCourseName())){
                courses.setCourseName(courseDto.getCourseName());
            }

            if(!courseDto.getCourseCode().isBlank() && !Objects.equals(courseDto.getCourseCode(), courses.getCourseCode())){
                courses.setCourseCode(courseDto.getCourseCode());
            }



            courses.setRegisteredBy(user);

            Courses courses1 =  coursesRepository.save(courses);

            return new Response<>(false,ResponseCode.SUCCESS,courses1,"New course added");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Courses> findCourseByUuid(String uuid) {
        try {
            Optional<Courses> optionalCourses =  coursesRepository.findFirstByUuid(uuid);

            return optionalCourses.map(courses -> new Response<>(false, ResponseCode.SUCCESS, courses, "Data found")).orElseGet(() -> new Response<>(true, ResponseCode.NO_RECORD_FOUND, "No record found with that ID"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Courses> findCourseByName(String name) {
        try {
            Optional<Courses> optionalCourses =  coursesRepository.findFirstByCourseName(name);

            return optionalCourses.map(courses -> new Response<>(false, ResponseCode.SUCCESS, courses, "Data found")).orElseGet(() -> new Response<>(true, ResponseCode.NO_RECORD_FOUND, "No record found with that name"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Page<Courses> getAllCourses(Pageable pageable) {
        try {
            Page<Courses>  coursesPage =  coursesRepository.findAllByDeletedFalseOrderByCreatedAtDesc(pageable);
            return  coursesPage;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response<Courses> deleteCourse(String uuid) {
        try {
            Optional<Courses> optionalCourses =  coursesRepository.findFirstByUuid(uuid);

            if(optionalCourses.isEmpty())
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that ID");

            Courses courses =  optionalCourses.get();
            coursesRepository.delete(courses);

            return new Response<>(false,ResponseCode.SUCCESS,"Department deleted successful");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }


}
