package com.example.data02.entity;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;       //queryDSL을 사용해야 함.
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
public class DslDynamicTest {
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
    @DisplayName("jpql 동적쿼리")
    void jpqlDynamicQuery(){
//        jpql을 직접 사용하는 경우 [동적쿼리]

//        searchType, keyword는 매개변수로 받았다고 가정한다.
        String searchType = "salary";
        String keyword = "1000";

        String jpql = "select e from Employee e ";

        if("name".equals(searchType)){
            jpql += "where e.name like '%' || :keyword || '%'";
        } else if("salary".equals(searchType)){
            jpql += "where e.salary > :keyword";
        }

        List<Employee> list = em.createQuery(jpql, Employee.class)
                .setParameter("keyword", keyword)
                .getResultList();

        System.out.println("list = " + list);

//        이런 동적쿼리 생성은 문자열을 기반으로 만들어지기 때문에
//        오류 찾기가 힘들고 가독성이 떨어진다.
    }

    @Test
    @DisplayName("QueryDSL 동적쿼리01")
    void dslDynamicQuery(){
        String searchType = "name";
        String keyword = "김철수";
        LocalDate date = LocalDate.of(2000, 1, 2);
//        위 데이터는 매개변수로 받았다고 가정한다.

//        쿼리 DSL로 동적 쿼리를 만들 때 2가지 방법을 사용한다.

//        1. BooleanBuilder를 사용한다.
//        BooleanBuilder는 where 절에 사용할 조건을 동적으로 만들어줄 수 있다.
//        and(), or()를 사용하여 쿼리를 동적으로 생성한다.
        BooleanBuilder builder = new BooleanBuilder();

        if("name".equals(searchType)){
            builder.and(employee.name.eq(keyword));
        } else if("salary".equals(searchType)){
            builder.and(employee.salary.gt(Integer.parseInt(keyword)));
        }

        if (date != null){
            builder.and(employee.hireDate.goe(date));
        }

        List<Employee> employeeList = queryFactory.selectFrom(employee)
                .where(builder)
                .fetch();

        System.out.println("employeeList = " + employeeList);
    }

    BooleanBuilder getWhere(String searchType, String keyword, LocalDate date){
        BooleanBuilder builder = new BooleanBuilder();

        if("name".equals(searchType)){
            builder.and(employee.name.eq(keyword));
        } else if("salary".equals(searchType)){
            builder.and(employee.salary.gt(Integer.parseInt(keyword)));
        }

        if (date != null){
            builder.and(employee.hireDate.goe(date));
        }
        return builder;
    }


    /**
     * ctrl+d : 한줄 복사
     */
    @Test
    @DisplayName("QueryDSL 동적쿼리02")
    void dslDynamicQuery2(){
        String searchType = "name";
        String keyword = "김철수";
        LocalDate date = LocalDate.of(2000, 1, 2);

        BooleanBuilder builder = new BooleanBuilder();

//        동적 쿼리 조건 생성 코드가 복잡하거나 반복적으로 여러 코드에서 사용된다면
//        메소드로 분리하여 코드를 깔끔하게 만들 수 있다.
        List<Employee> employeeList = queryFactory.selectFrom(employee)
//                .where(searchCond01(searchType, keyword, date))
//                BooleanBuilder에 조건이 생성되지 않으면(아래처럼 null을 꽂아넣으면)
//                알아서 where 절을 생성하지 않는다.
                .where(searchCond01(null, null, null))
                .fetch();

        System.out.println("employeeList = " + employeeList);
    }

    BooleanBuilder searchCond01(String searchType, String keyword, LocalDate date) {
        BooleanBuilder builder = new BooleanBuilder();

        // searchType.equals("name") 이렇게 쓸 수도 있지만 null이 들어올 경우
        // NPE가 발생할 수 있으므로 그냥 문자열 리터럴을 쓰는 게 낫다.
        if("name".equals(searchType)){
            builder.and(employee.name.eq(keyword));
        } else if("salary".equals(searchType)){
            builder.and(employee.salary.gt(Integer.parseInt(keyword)));
        }

        if (date != null){
            builder.and(employee.hireDate.goe(date));
        }

        return builder;
    }

    @Test
    @DisplayName("동적 쿼리 실습01")
    void dynamicQueryTask01() {
//        department를 조회하는 쿼리를 작성한다.
//        name과 officeLocation을 입력받아 부서를 조회한다.
//        둘 중 하나라도 일치하는 부서를 조회하고, 만약 둘 중 하나라도 null이면 조건을 생략
        queryFactory.selectFrom(department)
                .where(departmentSearchCond("개발", "A"))
                .fetch();

    }

    BooleanBuilder departmentSearchCond(String name, String officeLocation) {

        if (name == null || officeLocation == null) return null;

        return new BooleanBuilder().or(department.name.eq(name))
                .or(department.officeLocation.eq(officeLocation));
    }

    @Test
    @DisplayName("QueryDSL 동적쿼리03")
    void dslDynamicQuery03() {
        String searchType = "name";
        String keyword = "김철수";
        LocalDate date = LocalDate.of(2000, 1, 2);

//        2. where에서 콤마(,)를 사용하면 여러 조건을 and로 연결해준다.
//        where(조건1, 조건2) -> 조건1 and 조건2
//        where에 넘긴 조건이 null인 경우 null을 무시한다는 특징이 있다.
//        이 특징들을 이용하면 동적으로 조건을 생성할 수 있다.
        queryFactory.selectFrom(employee)
//                조건을 여러개 넣고 싶으면 콤마(,) 사용하면 됨.
//                혹시 조건 중 null이 들어가면 그 조건은 생략되고 나머지 조건만 where가 걸린다.
//                .where(employee.name.eq("김철수"), employee.salary.gt(10_000))
//                .where(employee.name.eq("김철수"), date == null ? null : employee.hireDate.after(date))
                .where(searchCond02(searchType, keyword))
                .fetch();

    }

//    where의 파라미터 타입은 Predicate이다.
    Predicate searchCond02(String searchType, String keyword) {

        if(searchType == null || keyword == null) return null;

        if("name".equals(searchType)) {
            return employee.name.eq(keyword);
        }

        return employee.salary.eq(Integer.parseInt(keyword));
    }

    @Test
    @DisplayName("동적 쿼리 실습02")
    void dynamicQueryTask02() {
//        사원 이름, 부서 이름, 부서 전화번호를 전달받아 모두 일치하는
//        사원 이름과 해당 사원의 소속 부서 정보를 조회한다.
//        모든 조건은 부분 일치 조건으로 사용한다.
//        만약 null인 경우 해당 조건을 생략
//        BooleanBuilder 사용X

        String empName = "김철수";
        String deptName = "개발";
        String deptPhone = "222";

        queryFactory.select(employee.name, employee.department)
                .from(employee)
                .where(
                        empNameCond(empName),
                        deptName == null ? null : employee.department.name.eq(deptName),
                        deptPhone == null ? null : employee.department.phone.eq(deptPhone)
                )
                .fetch();
    }

    Predicate empNameCond(String empName) {
        return empName == null ? null : employee.name.contains(empName);
    }

}







