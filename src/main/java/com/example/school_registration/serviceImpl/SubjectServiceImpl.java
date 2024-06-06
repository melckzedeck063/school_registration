package com.example.school_registration.serviceImpl;

import com.example.school_registration.dto.CourseDto;
import com.example.school_registration.dto.SubjectDto;
import com.example.school_registration.models.Courses;
import com.example.school_registration.models.Department;
import com.example.school_registration.models.Subjects;
import com.example.school_registration.models.UserAccount;
import com.example.school_registration.repositories.CoursesRepository;
import com.example.school_registration.repositories.SubjectRepository;
import com.example.school_registration.service.SubjectService;
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
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository  subjectRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private LoggedUser  loggedUser;

    @Override
    public Response<Subjects> createSubject(SubjectDto subjectDto) {
        try {
            UserAccount user  = loggedUser.getUser();

            Subjects subjects =  new Subjects();
            if(user == null) {
                return new Response<>(true, ResponseCode.UNAUTHORIZED,"unauthorized");
            }

            if(subjectDto.getSubjectName() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"course name can nt be null");
            }
            else {
                Optional<Subjects> optionalSubjects =  subjectRepository.findFirstBySubjectName(subjectDto.getSubjectName());

                if(optionalSubjects.isPresent())
                    return new Response<>(true,ResponseCode.DUPLICATE,"Subject with that name already exists");
            }


            if(subjectDto.getSubjectCode() == null){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"course code can nt be null");
            }
            else {
                Optional<Subjects> optionalSubjects =  subjectRepository.findFirstBySubjectCode(subjectDto.getSubjectCode());

                if(optionalSubjects.isPresent())
                    return new Response<>(true,ResponseCode.DUPLICATE,"Subject with that code already exists");
            }

            if(subjectDto.getCourseUuid() != null){
                Optional<Courses> optionalCourses = coursesRepository.findFirstByUuid(subjectDto.getCourseUuid());

                optionalCourses.ifPresent(subjects::setCourse);

                if(optionalCourses.isEmpty()){
                    return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that ID");
                }
            }

            if(!subjectDto.getSubjectName().isBlank() && !Objects.equals(subjectDto.getSubjectName(), subjects.getSubjectName())){
                subjects.setSubjectName(subjectDto.getSubjectName());
            }

            if(!subjectDto.getSubjectCode().isBlank() && !Objects.equals(subjectDto.getSubjectCode(), subjects.getSubjectCode())){
                subjects.setSubjectCode(subjectDto.getSubjectCode());
            }



            subjects.setRegisteredBy(user);

            Subjects  subjects1 =  subjectRepository.save(subjects);

            return new Response<>(false,ResponseCode.SUCCESS,subjects1,"New course added");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Subjects> findSubjectByUuid(String uuid) {
        try {
            Optional<Subjects> optionalSubjects = subjectRepository.findFirstByUuid(uuid);

            return optionalSubjects.map(student -> new Response<>(false, ResponseCode.SUCCESS, student, "Data found")).orElseGet(() -> new Response<>(true, ResponseCode.NO_RECORD_FOUND, "No record found with that ID"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Subjects> findSubjectByName(String name) {
        try {
            Optional<Subjects> optionalSubjects =  subjectRepository.findFirstBySubjectName(name);

            return optionalSubjects.map(subject -> new Response<>(false, ResponseCode.SUCCESS, subject, "Data found")).orElseGet(() -> new Response<>(true, ResponseCode.NO_RECORD_FOUND, "No record found with that name"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Page<Subjects> getAllSubjects(Pageable pageable) {
        try {
            Page<Subjects>  coursesPage =  subjectRepository.findAllByDeletedFalseOrderByCreatedAtDesc(pageable);
            return  coursesPage;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response<Subjects> deleteSubject(String uuid) {
        try {
            Optional<Subjects> optionalSubjects =  subjectRepository.findFirstByUuid(uuid);

            if(optionalSubjects.isEmpty())
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"No record found with that ID");


            Subjects subjects =  optionalSubjects.get();
            coursesRepository.delete(subjects.getCourse());

            return new Response<>(false,ResponseCode.SUCCESS,"subjects deleted successful");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

}
