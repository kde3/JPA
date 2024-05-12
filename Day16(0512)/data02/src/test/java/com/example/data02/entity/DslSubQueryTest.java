package com.example.data02.entity;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
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

import static com.example.data02.entity.QDepartment.department;
import static com.example.data02.entity.QEmployee.employee;

@SpringBootTest
@Transactional @Commit
public class DslSubQueryTest {
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
    @DisplayName("서브 쿼리 : where 1")
    void subQuery01(){
//        급여가 가장 높은 사원 조회
//        select e from Employee e where e.salary = (select max(e2.salary) from Employee e2)

        QEmployee qEmployee = employee; // 기본제공 Q타입 객체의 별칭은 이름과 동일한 employee이다.
        QEmployee subEmployee = new QEmployee("e2"); // 생성된 Q객체는 우리가 지정한 e2로 별칭 설정이 된다.

//        QueryDSL에서 서브쿼리를 사용하는 경우 JPAExpressions 타입을 사용한다.
        List<Employee> empList = queryFactory.selectFrom(qEmployee)
                .where(qEmployee.salary.eq(
                        JPAExpressions.select(subEmployee.salary.max())
                                .from(subEmployee)
                )).fetch();

        System.out.println("empList = " + empList);
    }

    @Test
    @DisplayName("서브 쿼리 : where 2")
    void subQuery02(){
//        부서 별 급여가 가장 높은 사원 조회 (조건에 In을 사용하기)
        QEmployee e = new QEmployee("e");
        QEmployee e2 = new QEmployee("e2");

        List<Employee> empList = queryFactory.selectFrom(e)
                .where(e.salary.in(
                        JPAExpressions.select(e2.salary.max())
                                .from(e2).join(e2.department, department)
                                .groupBy(department.name)
                )).fetch();

        System.out.println("empList = " + empList);
    }

    @Test
    @DisplayName("서브 쿼리 : select 1")
    void scalar01() {
        QEmployee subEmployee = new QEmployee("subEmployee");

        List<Tuple> tupleList = queryFactory.select(employee.name,
                        JPAExpressions.select(subEmployee.salary.avg())
                                .from(subEmployee))
                .from(employee)
                .fetch();

        tupleList.forEach(tuple -> {
            System.out.println("name : " + tuple.get(employee.name));
            System.out.println("salary avg : " + tuple.get(
                    JPAExpressions.select(subEmployee.salary.avg()).from(subEmployee))
            );
        });
    }

    @Test
    @DisplayName("서브 쿼리 : select 2")
    void scalar02(){
//        서브쿼리를 아래와 같이 분리할 수 있다. (가독성, 유지보수, 재사용율 높아짐)
        JPQLQuery<String> subQuery = JPAExpressions.select(department.officeLocation)
                .from(department)
                .where(department.id.eq(employee.department.id));

        List<Tuple> tupleList = queryFactory.select(employee.name, subQuery)
                .from(employee)
                .fetch();

        tupleList.forEach( tuple -> {
            System.out.println("name : " + tuple.get(employee.name));
//            서브 쿼리를 분리하면 변수명으로 튜플 조회가 가능하다.
            System.out.println("office location : " + tuple.get(subQuery));
        } );
    }

    @Test
    @DisplayName("서브 쿼리 : from 1")
    void inlineView01(){
//        하이버네이트 6.1버전 부터 from 절 서브쿼리 사용이 가능하다.
//        QueryDSL은 from절 서브쿼리가 어느정도 안정화된 버전이 나오면 개발을 해준다고 했다.
//        즉, 아직은 from절 서브쿼리를 사용하고 싶다면, entityManager를 이용하여 jpql을 작성해야함

//        QEmployee subEmployee = new QEmployee("subEmployee");
//        queryFactory.select(employee)
//                .from(
//                        JPAExpressions.select(subEmployee)
//                                .from(subEmployee)
//                ).fetch();
    }

}











