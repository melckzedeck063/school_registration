package com.example.school_registration.serviceImpl;

import com.example.school_registration.dto.StudentDto;
import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Students;
import com.example.school_registration.models.UserAccount;
import com.example.school_registration.repositories.CoursesRepository;
import com.example.school_registration.repositories.StudentsRepository;
import com.example.school_registration.service.StudentsService;
import com.example.school_registration.utils.Response;
import com.example.school_registration.utils.ResponseCode;
import com.example.school_registration.utils.userextractor.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentsServiceImpl implements StudentsService {

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private CoursesRepository coursesRepository;

    @Override
    public Response<Students> createStudent(StudentDto studentDto) {
        try {
            UserAccount user = loggedUser.getUser();

            Students students =  new Students();

            if(user == null)
                return new Response<>(true,ResponseCode.UNAUTHORIZED,"Unauthorized");

            if(studentDto.getFirstname() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"firstname can not be null");
            }

            if(studentDto.getLastname() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"lastname can not be null");
            }

            if(studentDto.getGender() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"gender can not be null");
            }

            if(studentDto.getCourseUuid() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"course can not be null");
            }
            else {
                Optional<Courses> optionalCourses =  coursesRepository.findFirstByUuid(studentDto.getCourseUuid());

                optionalCourses.ifPresent(students::setCourse);
                if(optionalCourses.isEmpty())
                    return new Response<>(true,ResponseCode.NULL_ARGUMENT,"No record found with that ID");
            }

            if(studentDto.getAge() <= 0 || studentDto.getAge() >= 30){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"Age out of bound");
            }


            if(!studentDto.getFirstname().isBlank() && !Objects.equals(studentDto.getFirstname(), students.getFirstname())){
                students.setFirstname(studentDto.getFirstname());
            }

            if(!studentDto.getLastname().isBlank() && !Objects.equals(studentDto.getLastname(), students.getLastname())){
                students.setLastname(studentDto.getLastname());
            }

            if(!studentDto.getGender().isBlank() && !Objects.equals(studentDto.getGender(), students.getGender())){
                students.setGender(studentDto.getGender());
            }

            students.setRegisteredBy(user);
            students.setAge(studentDto.getAge());

            Students students1 =  studentsRepository.save(students);

            return new Response<>(false,ResponseCode.SUCCESS,students1,"Student registered successful");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Students> getStudentByUuid(String uuid) {
        try {
            Optional<Students>  optionalStudents =  studentsRepository.findFirstByUuid(uuid);
            return optionalStudents.map(students -> new Response<>(false, ResponseCode.SUCCESS, students, "Data found")).orElseGet(() -> new Response<>(true, ResponseCode.NO_RECORD_FOUND, "No record found with that ID"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Students> getStudentByName(String fname,String lname) {
        try {
            Optional<Students> optionalStudents = studentsRepository.findFirstByFirstnameAndLastnameAndDeletedFalse(fname,lname);

            return optionalStudents.map(students -> new Response<>(false, ResponseCode.SUCCESS, students, "Record found")).orElseGet(() -> new Response<>(true, ResponseCode.NO_RECORD_FOUND, "No record found with  that ID"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Page<Students> getAllStudents(Pageable pageable) {
        try {
            Page<Students> studentsPage = studentsRepository.findAllByDeletedFalseOrderByCreatedAtDesc(pageable);

            return studentsPage;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new PageImpl<>(new ArrayList<>());
    }

    @Override
    public Response<Students> deleteStudent(String uuid) {
        try {
            Optional<Students> optionalStudent = studentsRepository.findFirstByUuid(uuid);
            if(optionalStudent.isEmpty()){
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that ID");
            }

            Students student = optionalStudent.get();

            studentsRepository.delete(student);

            return new Response<>(false,ResponseCode.SUCCESS,"Student deleted successful");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Page<Students> getStudentByCourse(Courses course, Pageable pageable) {
        try {
            Page<Students> studentsPage =  studentsRepository.findAllByCourseIsAndDeletedFalse(course,pageable);

            return studentsPage;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new PageImpl<>(new ArrayList<>());
    }
}
