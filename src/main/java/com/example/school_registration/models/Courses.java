package com.example.school_registration.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "courses")
@SQLDelete(sql = "UPDATE courses SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Courses extends  BaseEntity{
    @Column(name = "course")
    private String  courseName;

    @Column(name = "courseCode")
    private String courseCode;

    @ManyToOne
    @JoinColumn(name = "department", referencedColumnName = "uuid")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "registeredBy", referencedColumnName = "uuid")
    private UserAccount registeredBy;
}
