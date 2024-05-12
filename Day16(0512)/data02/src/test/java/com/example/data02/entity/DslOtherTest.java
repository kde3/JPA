package com.example.data02.entity;

import com.example.data02.dto.DepartmentDTO;
import com.example.data02.dto.EmployeeDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Expression;
import java.time.LocalDate;
import java.util.List;

import static com.example.data02.entity.QDepartment.department;
import static com.example.data02.entity.QEmployee.employee;

@SpringBootTest
@Transactional
@Commit
public class DslOtherTest {
    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(em);

        Department dep1 = Department.builder()
                .name("개발")
                .phone("02-1111-1111")
                .officeLocation("A")
                .build();

        Department dep2 = Department.builder()
                .name("디자인")
                .phone("02-2222-2222")
                .officeLocation("B")
                .build();

        em.persist(dep1);
        em.persist(dep2);

        Employee employee1 = Employee.builder()
                .name("김철수")
                .salary(10_000)
                .hireDate(LocalDate.of(2000, 10, 10))
                .email("test@naver.com")
                .department(dep1)
                .build();

        Employee employee2 = Employee.builder()
                .name("홍길동")
                .salary(20_000)
                .hireDate(LocalDate.of(2010, 1, 1))
                .email("hong@naver.com")
                .department(dep1)
                .build();

        Employee employee3 = Employee.builder()
                .name("이지웅")
                .salary(18_000)
                .hireDate(LocalDate.of(2020, 7, 22))
                .email("woong@naver.com")
                .department(dep2)
                .build();

        em.persist(employee1);
        em.persist(employee2);
        em.persist(employee3);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("조회 결과 연결하기")
    void concat() {
        List<String> concatList = queryFactory.select(employee.name.concat(" : ").concat(employee.email))
                .from(employee)
                .fetch();
        System.out.println("concatList = " + concatList);
    }

    @Test
    @DisplayName("문자열이 아닌 값과 연결하기")
    void concat2() {
//        sql 또는 jpql에서는 concat에 문자열이 아닌 값을 넣어도 자동 형변환된다.
//        그러나 queryDSL은 concat의 매개변수 타입이 String이므로 문자열 타입을 넣어야한다.
//        이런 경우 해당 필드에 stringValue()를 사용하여 문자열 값으로 변경해주면 된다.
        List<String> concatList = queryFactory.select(employee.name.concat(" : ").concat(employee.salary.stringValue()))
                .from(employee)
                .fetch();

        System.out.println("concatList = " + concatList);
    }

    @Test
    @DisplayName("case 문")
    void caseWhenThen() {
        List<String> list = queryFactory.select(employee.department.name
//                        when에 값만 넣으면 = 조건만 사용 가능하다.
                                .when("개발").then("야근")
                                .when("디자인").then("퇴근")
                                .otherwise("퇴사") // otherwise() 는 필수이다.
                )
                .from(employee)
                .fetch();

        System.out.println("list = " + list);


    }

    @Test
    @DisplayName("case문2")
    void caseWhenThen2() {
//        같다(=) 조건 외에 여러 조건을 사용하고 싶으면 CaseBuilder를 활용한다.
        StringExpression salaryGrade = new CaseBuilder().when(employee.salary.gt(20_000)).then("부자")
                .when(employee.salary.gt(15_000)).then("평균")
                .when(employee.salary.gt(5_000)).then("거지")
                .otherwise("모름");

        List<String> list = queryFactory.select(salaryGrade)
                .from(employee)
                .fetch();

        System.out.println("list = " + list);
    }

    @Test
    @DisplayName("상수 조회하기")
    void constantSelect() {
        List<Tuple> tupleList = queryFactory.select(employee, Expressions.constant(100))
                .from(employee)
                .fetch();

        for (Tuple tuple : tupleList) {
            System.out.println("emp : " + tuple.get(employee));
            System.out.println("constant : " + tuple.get(Expressions.constant(100)));
        }
    }

    @Test
    @DisplayName("DTO 반환 받기")
    void returnDTO() {
//        jpql
//        List<EmployeeDTO> resultList = em.createQuery("""
//                            select new com.example.data02.dto.EmployeeDTO(e.name, e.salary, e.hireDate)
//                            from Employee e
//                        """, EmployeeDTO.class)
//                .getResultList();

//        QueryDSL 에서는 프로젝션을 DTO로 설정하기 위해 Projections 타입을 사용한다.
//        Projections로  DTO를 반환받는 방법은 3가지가 있다.

//        1. fields() : 필드 직접 접근
//        - 필드에 직접 접근하므로 프로젝션 순서, 개수 상관없이 반환받을 수 있다.
//        - 생성자나 setter 없이도 DTO를 반환 받을 수 있다.
//        - 필드가 private이여도 상관없다. (리플렉션을 사용하기 때문)
//        단, QueryDSL이 DTO객체를 생성할 때 기본생성자를 사용하므로 DTO에 기본생성자는 필수이다.
        List<EmployeeDTO> empList = queryFactory.select(
                        Projections.fields(EmployeeDTO.class, employee.name, employee.hireDate)
                )
                .from(employee)
                .fetch();

        System.out.println("empList = " + empList);

//        2. bean() : setter로 접근
//        - setter를 사용하므로 프로젝션 순서, 개수 상관없이 DTO를 반환할 수 있다.
//        기본 생성자가 필수이다.
        List<EmployeeDTO> empList2 = queryFactory.select(
                        Projections.bean(EmployeeDTO.class, employee.name, employee.salary)
                )
                .from(employee)
                .fetch();
        System.out.println("empList2 = " + empList2);

//        3. constructor() : 생성자로 초기화
//        - 생성자를 사용하므로 dto에 만들어놓은 생성자의 파라미터 순서와 개수에 맞게 전달해야한다.
//        기본 생성자가 필요없다.
        List<EmployeeDTO> empList3 = queryFactory.select(
                        Projections.constructor(EmployeeDTO.class, employee.name, employee.salary, employee.hireDate)
                )
                .from(employee)
                .fetch();

        System.out.println("empList3 = " + empList3);
    }

    @Test
    @DisplayName("Dto 리턴 받기2")
    void returnDTO2() {
//        만약 엔티티의 필드명과 DTO의 필드명이 다르다면?
        List<DepartmentDTO> deptList = queryFactory.select(
                        Projections.bean(DepartmentDTO.class,
                                department.name, department.officeLocation, department.phone)
                )
                .from(department)
                .fetch();
//        확인해보니 필드명이 일치하지 않아서 데이터가 바인딩되지 않는다.
        System.out.println("deptList = " + deptList);

//        이 경우 마이바티스와 동일하게 별칭을 활용하면 된다.
        List<DepartmentDTO> deptList2 = queryFactory.select(
                        Projections.bean(DepartmentDTO.class,
                                department.name,
                                department.officeLocation.as("office"),
                                department.phone.as("phoneNumber"))
                ).from(department)
                .fetch();

        System.out.println("deptList2 = " + deptList2);

    }

}










