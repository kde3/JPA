package com.example.data02.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class DslJoinTest {
    @PersistenceContext
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp(){
        /*
        QueryDSL
        JPQL을 생성해주는 기술(오픈소스 프레임워크)
        문자열이 아닌 메서드 체이닝 방식으로 JPQL을 만들 수 있기 때문에 문법 오류를 컴파일
        단계에서 확인할 수 있으며, 직관적인 동적쿼리를 작성할 수 있다는 장점이 있다.
        QueryDSL은 실제 엔티티가 아닌 엔티티의 정보를 담은 Q타입 객체를 사용하며,
        Q타입 클래스는 설정된 특정 위치에 생성된다.

        표준 기술이 아니지만 JPA를 사용한다면 필수적으로 가져가는 기술이다.
        표준이 아니기 때문에 설정의 번거로움과 버전 변경시 새로운 설정 방법을 찾아야한다는
        단점이 있다.
         */
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
                .hireDate(LocalDate.of(2000,10,10))
                .email("test@naver.com")
                .department(dep1)
                .build();

        Employee employee2 = Employee.builder()
                .name("홍길동")
                .salary(20_000)
                .hireDate(LocalDate.of(2010,1,1))
                .email("hong@naver.com")
                .department(dep1)
                .build();

        Employee employee3 = Employee.builder()
                .name("이지웅")
                .salary(18_000)
                .hireDate(LocalDate.of(2020,7,22))
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
    @DisplayName("기본 Join")
    void basicJoin() {
        QEmployee qEmployee = QEmployee.employee;                    //기본 별칭은 필드 이름과 똑같음.
        QDepartment qDepartment = new QDepartment("d");     //별칭 지정

        List<Employee> empList1 = queryFactory.selectFrom(qEmployee)
                .join(qEmployee.department)     //join할 대상을 파라미터로 전달
                .fetch();

//        join 후 department를 사용하기가 불편함.
        List<Employee> empList2 = queryFactory.selectFrom(qEmployee)
                .join(qEmployee.department)
                .where(qEmployee.department.name.eq("개발"))
                .fetch();

//        위 문제를 해결하려면 별칭 지정하면 됨(qEmployee.department 대신 qDepartment 사용).
        queryFactory.selectFrom(qEmployee)
                .join(qEmployee.department, qDepartment)        //join(조인할 대상, 별칭) -> 별칭 설정이 끝나있는 Q타입을 넣는다.
                .where(qDepartment.name.ne("test"))
                .fetch();
    }

    @Test
    @DisplayName("where절에 의한 join")
    void whereJoin() {
        queryFactory.selectFrom(QEmployee.employee)
                //연관관계 타고 들어가기 때문에 자동 join이 됨.
                .where(QEmployee.employee.department.officeLocation.eq("A"))
                .fetch();
    }

}