package com.example.data01.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="tbl_employee")
@Getter @ToString                   //setter는 넣지X
//AllArgsConstructor를 사용하여 Builder 패턴을 적용할 것이기 때문에
//NoArgsConstructor를 명시해야 한다(엔티티는 기본생성자 필수)
//그런데 기본생성자가 존재한다면 불필요한 엔티티가 생성될 가능성이 열린다
//기본생성자를 없애고 싶은데 없으면 JPA가 엔티티 객체를 내부적으로 사용할 때 문제가 생김
//그래서 접근제한자를 Protected로 변경해서 사용함.
//JPA는 protected 레벨의 생성자까지는 사용이 가능함(프록시 객체를 만들때 상속하여 사용하므로.)
@NoArgsConstructor(access = AccessLevel.PROTECTED) //이러면 무조건 builder를 사용해야 함(기본생성자로 객체 생성 불가).
@Builder @AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue
    @Column(name = "EMPLOYEE_ID")
    private Long id;
    private String name;
    private int salary;
    private String email;
    private LocalDate hireDate;

    //Builder는 생성자 위에도 붙일 수 있음.
    //생성자 위에 붙이면 필드 순서와 상관이 없어지고, AllAgrs가 아니라 내가 원하는 만큼 필드 초기화 가능하다.
}
