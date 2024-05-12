package com.example.data02.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "tbl_department")
@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Department {
    @Id @GeneratedValue
    @Column(name = "department_id")
    private Long id;
    private String name;
    private String phone;
    private String officeLocation;
}









