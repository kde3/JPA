package com.example.data02.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @ToString
@NoArgsConstructor
public class EmployeeDTO {
    private String name;
    private int salary;
    private LocalDate hireDate;

    public EmployeeDTO(String name, int salary, LocalDate hireDate) {
        this.name = name;
        this.salary = salary;
        this.hireDate = hireDate;
    }
}
