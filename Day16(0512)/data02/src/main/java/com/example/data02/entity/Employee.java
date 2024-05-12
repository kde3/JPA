package com.example.data02.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_employee")
@Getter @ToString(exclude = "department")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Employee {
    @Id @GeneratedValue
    @Column(name = "EMPLOYEE_ID")
    private Long id;
    private String name;
    private int salary;
    private String email;
    private LocalDate hireDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

}











