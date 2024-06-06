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
@Entity(name = "departments")
@SQLDelete(sql = "UPDATE departments SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Department extends BaseEntity {

    @Column(name = "department")
    private String  department;

    @Column(name = "code")
    private String deptCode;

    @ManyToOne
    @JoinColumn(name = "hod", referencedColumnName = "uuid")
    private UserAccount hod;

    @ManyToOne
    @JoinColumn(name = "registeredBy", referencedColumnName = "uuid")
    private UserAccount registeredBy;
}



