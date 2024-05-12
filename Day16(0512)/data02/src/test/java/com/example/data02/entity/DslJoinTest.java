package com.example.data02.entity;

import com.querydsl.core.Tuple;
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
    void basicJoin(){
        QEmployee qEmployee = employee;
        QDepartment qDepartment = department;

        List<Employee> empList1 = queryFactory.selectFrom(qEmployee)
                .join(qEmployee.department) // join할 대상을 파라미터로 전달
                .fetch();


//        Join 후 department를 사용하기가 불편하다.
        List<Employee> empList2 = queryFactory.selectFrom(qEmployee)
                .join(qEmployee.department)
                .where(qEmployee.department.name.eq("개발"))
                .fetch();

//      qEmployee.department 대신 별칭을 지정할 수 있다.
        queryFactory.selectFrom(qEmployee)
                .join(qEmployee.department, qDepartment) // join(조인할 대상, 별칭) -> 별칭은 q타입을 사용
                .where(qDepartment.name.ne("test"))
                .fetch();
    }

    @Test
    @DisplayName("where절에 의한 join")
    void whereJoin01(){
        queryFactory.selectFrom(employee)
                .where(employee.department.officeLocation.eq("A"))
                .fetch();
    }

    @Test
    @DisplayName("Outer Join")
    void outerJoin(){
        List<Employee> empList = queryFactory.selectFrom(employee)
                .leftJoin(employee.department, department)
                .where(department.name.eq("개발"))
                .fetch();

        System.out.println("empList = " + empList);
    }

    @Test
    @DisplayName("Cross Join(카르테시안 곱)")
    void crossJoin(){
//        주로 연관관계가 없는 필드로 조인을 걸고 싶거나
//        아예 무관한 엔티티끼리 조인을 걸고 싶을 때 사용했었다.
        List<Tuple> tupleList = queryFactory.select(employee, department)
                .from(employee, department)
                .fetch();

        tupleList.forEach(System.out::println);

        em.persist(Employee.builder().email("A").build());

        List<Tuple> tupleList2 = queryFactory.select(employee, department)
                .from(employee, department)
                .where(employee.email.eq(department.officeLocation))
                .fetch();

        System.out.println("=============================================");
        tupleList2.forEach(System.out::println);

//        예전 버전에서는 연관관계 없는 조인이 불가능하여 어쩔 수 없이 사용하는 경우가 있었으나
//        지금은 하이버 네이트 버전이 올라가면서 연관관계가 없는 join On조건 사용 가능해졌다.
    }

    @Test
    @DisplayName("on절이 없는 join")
    void crossJoin2(){
//        join을 명시하여 사용하는 경우 연관관계를 이용하지 않으면
//        on절을 사용하지 않을 시 카르테시안 곱이 발생될 수 있다.
        List<Tuple> tupleList = queryFactory.select(employee, department)
                .from(employee)
                .join(department)
                .fetch();

        tupleList.forEach(System.out::println);
    }

    @Test
    @DisplayName("on절을 사용하여 연관관계 없는 조건 넣기")
    void joinOn(){
        em.persist(Employee.builder().email("A").build());
        em.flush();
        em.clear();

//        select에 정확히 조회대상(프로젝션)을 명시하여 employee, department 엔티티를
//        둘 다 가져오는 경우( 둘 다 영속화 )
//        List<Tuple> tupleList = queryFactory.select(employee, department)
//                .from(employee)
//                .join(department).on(employee.email.eq(department.officeLocation))
//                .fetch();
//
//        em.find(Department.class, 2L); // select쿼리 생성됨
//        em.find(Department.class, 1L); // 영속 상태이므로 select 쿼리 발생 안함
//        em.find(Employee.class, 4L); // 영속 상태이므로 select 쿼리 발생 안함
//
//        tupleList.forEach(System.out::println);

//        조인을 사용하였으나 department를 조건에서만 사용하고 프로젝션으로 지정하지 않은경우
//        Employee만 영속화
        List<Employee> empList = queryFactory.select(employee)
                .from(employee)
                .join(department).on(employee.email.eq(department.officeLocation))
                .where(department.phone.isNotNull())
                .fetch();

        empList.forEach(System.out::println);

        em.find(Employee.class, 4L);
        em.find(Department.class, 1L);

    }

    @Test
    @DisplayName("fetch join")
    void fetchJoin(){
        em.flush();
        em.clear();

        List<Employee> empList = queryFactory.selectFrom(employee)
                .join(employee.department, department).fetchJoin()
                .where(employee.id.gt(1L))
                .fetch();

        empList.forEach(System.out::println);

        empList.forEach(emp -> System.out.println(emp.getDepartment()));
    }
}









