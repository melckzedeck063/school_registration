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
@Entity(name = "subjects")
@SQLDelete(sql = "UPDATE subjects  SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Subjects extends BaseEntity {

    @Column(name = "subjectName")
    private String  subjectName;

    @Column(name = "subjectCode")
    private String subjectCode;

    @ManyToOne
    @JoinColumn(name = "course", referencedColumnName = "uuid")
    private Courses course;

    @ManyToOne
    @JoinColumn(name = "registeredBy", referencedColumnName = "uuid")
    private UserAccount registeredBy;
}
