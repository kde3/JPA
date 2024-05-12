package com.example.data02.entity;

import com.querydsl.core.QueryResults;
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

import static com.example.data02.entity.QEmployee.employee;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class DslTest {
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
    void loadTest(){
        QEmployee employee = QEmployee.employee;

        Employee foundEmp = queryFactory.select(employee)
                .from(employee)
                .fetchFirst();

        System.out.println("foundEmp = " + foundEmp);
    }

//    ======================================================
    @Test
    @DisplayName("Q타입 생성하기")
    void createQType(){
//        1. 별칭 지정하여 생성
//        QEmployee qEmp1 = new QEmployee("e");
//        2. 기본 객체 사용
//        QEmployee qEmp2 = QEmployee.employee;

//        일반적으로 기본 객체를 사용한다.
//        기본객체는 static이므로 static import를 활용하면 편하게 사용이 가능하다.
        QEmployee qEmployee = employee;
    }

    @Test
    @DisplayName("기본 select")
    void select01(){
//        DSL은 queryFactory를 이용하여 jpql을 생성한다.
        Employee foundEmp = queryFactory.select(employee) // select에 Q객체를 사용하면 전체 필드 조회
                .from(employee) // 엔티티가 아닌 Q객체를 대상으로한다.
                .where(employee.email.eq("test@naver.com"))
                .fetchOne(); // 단건 조회는 fetchOne() 사용

        System.out.println("foundEmp = " + foundEmp);

        List<Employee> empList = queryFactory.selectFrom(employee) // select와 from을 동시에 설정할 수 있다.
                .where(employee.name.ne("김철수")) // ne -> !=
                .fetch(); // 여러 행 조회는 fetch() 사용
    }

    @Test
    @DisplayName("여러 조건 활용")
    void select02(){
//        gt() : 초과
//        goe() : 이상
//        lt() : 미만
//        loe() : 이하
        List<Employee> empList = queryFactory.selectFrom(employee)
                .where(employee.salary.gt(10_000))
                .fetch();

//        in(a, b, c, ...)
//        notIn(a, b, c, ...)
//        between(a, b)
        List<Employee> list1 = queryFactory.selectFrom(employee)
//                .where(employee.salary.in(1,2,3,4))
                .where(employee.id.in(List.of(1L, 2L)))
                .fetch();

        List<Employee> list2 = queryFactory.selectFrom(employee)
                .where(employee.salary.between(1_000, 10_000))
                .fetch();

//        like('keyword'), notList('keyword')
//        contains('keyword') : %keyword% 자동으로 % 포함시켜줌
//        startWith('keyword') : keyword%
//        endWith('keyword') : %keyword

        queryFactory.selectFrom(employee)
                .where(employee.email.like("%aa%")) //like는 %, _ 직접 입력
                .fetch();
    }

    @Test
    @DisplayName("and, or 테스트")
    void andOrTest() {
        queryFactory.selectFrom(employee)
                .where(employee.name.eq("김철수")
                        .and(employee.salary.goe(10_000)))
                .fetch();

        queryFactory.selectFrom(employee)
                .where(employee.hireDate.after(LocalDate.of(2000,1,1))
                        .or(employee.salary.isNotNull()))
                .fetch();

//        end를 다음과 같이도 사용 가능***
        queryFactory.selectFrom(employee)
                //where 내부에서 ,를 사용ㅎ여 여러 조건을 넘겨주면 and 처리
                .where(employee.name.startsWith("김"), employee.department.isNull())
                .fetch();
    }

    //        -----------------------------------------

    @Test
    @DisplayName("정렬하기")
    void sorting() {
        List<Employee> empList1 = queryFactory.selectFrom(employee)
                .where(employee.salary.notIn(10_000))
                .orderBy(employee.salary.desc())
                .fetch();
        System.out.println("empList1 = " + empList1);

//        정렬 조건 2개 이상 설정
        List<Employee> empList2 = queryFactory.selectFrom(employee)
                .orderBy(employee.salary.desc(), employee.hireDate.asc())
                .fetch();
        System.out.println("empList2 = " + empList2);

//        null데이터 순서 정하기
        em.persist(Employee.builder().name("test").salary(10_000).build());
        List<Employee> empList3 = queryFactory.selectFrom(employee)
                .orderBy(employee.email.desc().nullsLast())     //null 가장 마지막에 위치키킴
                .fetch();
        System.out.println("empList3 = " + empList3);
    }

    @Test
    @DisplayName("실행 메서드")
    void executeMethod() {
//        쿼리 dsl에서 실행을 담당하는 메서드
//        1. fetchOne() : 단건 조회, 결과 없으면 null 반환. 2개 이상 조회되면 예외발생
//        2. fetch() : 여러 건 조회, 리스트를 반환하고 결과가 없으면 빈 리스트를 반환
//        3. fetchFirst() : 여러 건 ㅈ호회되어도 하나의 결과만 ㅂㄴ호
        Employee employee1 = queryFactory.selectFrom(employee)
                .fetchFirst();
        System.out.println("employee1 = " + employee1);

//        4. fetchCount() : 카운트 쿼리로 변경해서 실행(deprected) : 사용하는 건 권장하지 X
        long count = queryFactory.selectFrom(employee)
                .fetchCount();

        System.out.println("count = " + count);

//        5. fetchResults() : 페이징 처리 결과 반환. total 쿼리도 자동 생성하여 실행한다.
        QueryResults<Employee> results = queryFactory.selectFrom(employee)
                .orderBy(employee.id.desc())
                .offset(0)      // 시작행 설정(0부터 시작)
                .limit(2)       // 몇 건 조회인지 서정
                .fetchResults();    //total count 쿼리가 최적화되지 않기 때문에 사용빈도가 떨어졌다.

        System.out.println("results count" + results.getTotal());
        System.out.println("results.limit() = " + results.getLimit());
        System.out.println("result" + results.getResults());
    }

    @Test
    @DisplayName("반환타입")
    void returnType() {
//        반환타입
//        1. 엔티티 타입 : select()에 Q타입을 사용하면 자동으로 해당 엔티티 타입이 반환됨(all 반환)
//        2. 기본 타입 : 프로젝션으로 단일 필드를 지정하면 해당 필드의 타입으로 반환된다.
        List<String> names = queryFactory.select(employee.name)
                .from(employee)
                .fetch();

//        3. Tuple 타입 : 프로젝션으로 여러 필드를 지정하면 Tuple 타입으로 반환된다.
        List<Tuple> tupleList = queryFactory.select(employee.name, employee.email)
                .from(employee)
                .fetch();

//        Tuple 타입은 쿼리 dsl에서 제공하는 타입이며 다음과 같이 사용한다.
        for (Tuple tuple : tupleList) {
//            get(조회할 때 사용한 프로젝션);
            String name = tuple.get(employee.name);
            String email = tuple.get(employee.email);
            System.out.println(name + " : " + email);
        }
    }

    @Test
    @DisplayName("group by와 집계함수")
    void groupBy() {
//        count(), sum(), avg(). min(), max() 모두 지원
        Long count = queryFactory.select(employee.count())
                .from(employee)
                .fetchOne();//무조건 하나만 나오기 때문에

        System.out.println("count = " + count);

        Double avg = queryFactory.select(employee.salary.avg())
                .from(employee)
                .fetchOne();
        System.out.println("avg = " + avg);

//        group by와 having을 지원함
        List<Tuple> list = queryFactory.select(employee.department.name, employee.salary.avg())
                .from(employee)
                .groupBy(employee.department.name)
                .fetch();

        for (Tuple tuple : list) {
            String deptName = tuple.get(employee.department.name);
            Double salAvg = tuple.get(employee.salary.avg());
            System.out.println(deptName + " : " + salAvg);
        }
    }
}









