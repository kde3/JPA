package com.example.data02.repository;

import com.example.data02.entity.Department;
import com.example.data02.entity.Employee;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class DepartmentJpaRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    DepartmentJpaRepository departmentJpaRepository;

    @BeforeEach
    void setUp() {
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

    @AfterEach
    void tearDown() {
    }

    @Test
    void findById() {
        Department foundDept = departmentJpaRepository.findById(1L).orElse(null);
        assertThat(foundDept).isNotNull()
                .extracting(Department::getName)
                .isEqualTo("개발");
    }

    @Test
    void findByName() {
        Department foundDept = departmentJpaRepository.findByName("디자인").orElse(null);

        assertThat(foundDept).isNotNull()
                .extracting("name")
                .isEqualTo("디자인");
    }

    @Test
    void findByPhoneContaining() {
        List<Department> deptList = departmentJpaRepository.findByPhoneContaining("1111");

//        노란색 X면 검증 실패
        assertThat(deptList).hasSize(1)     // 1개만 나와야 함.
                .extracting("name")
                .containsExactly("개발");
    }

    @Test
    void findByEmployeeCountGoe() {
        List<Department> deptList = departmentJpaRepository.findByEmployeeCountGoe(2);

        assertThat(deptList)
                .hasSize(1)
                .extracting("name")
                .containsExactly("개발");
    }
}