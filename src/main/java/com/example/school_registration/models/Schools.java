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
@Entity(name = "schools")
@SQLDelete(sql = "UPDATE schools SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Schools extends BaseEntity{

    @Column(name="school")
    private String school;

    @Column(name = "regNo")
    private String regNo;

    @Column(name = "region")
    private String region;

    @Column(name= "district")
    private String district;

    @Column(name = "students")
    private String students;

    @ManyToOne
    @JoinColumn(name = "registeredBy")
    private UserAccount registeredBy;


}
