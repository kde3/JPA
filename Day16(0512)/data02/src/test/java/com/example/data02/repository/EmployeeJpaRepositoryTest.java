package com.example.data02.repository;

import com.example.data02.entity.Employee;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

//test 코드는 항상 똑같은 결과를 볼 수 있도록 짜는게 좋음.
//나중에 확인할 때 에러가 났을 경우, 로직을 다시 봐야하기 때문.
//또한 순서와 상관없이 짜는게 중요. 실행 순서가 정해져 있지 않기 때문(아마 알파벳 순으로 실행하는 것 같음)
//즉, 독립적이고 순서와 상관없이 짜야 함.
@SpringBootTest
@Transactional @Commit
class EmployeeJpaRepositoryTest {
    @Autowired
    EmployeeJpaRepository employeeJpaRepository;

    @Autowired
    EntityManager em;

    Employee emp;

    @BeforeEach
    void setUp() {
        emp = Employee.builder()
                .name("홍길동")
                .salary(10_000)
                .email("aaa@naver.com")
                .hireDate(LocalDate.of(2000,1,1))
                .build();
    }

    @Test
    void save() {
    }


    // 보통 test 코드를 전체 실행으로 test하므로 알아보기 쉽게 한글로 작성하게 되는데
//    @DisplayName을 쓸 수도 있고, 아예 메소드 이름을 한글로 작성하기도 한다. 취향차이
//    설정 >
    @Test
    @DisplayName("식별자로 조회")
    void findById() {
        employeeJpaRepository.save(emp);

//        Optional로 감쌌으니 벗겨야 하는데, 이때 get이나 orElse를 사용할 수 있음.
//        get을 쓰면 null일때 에러가 나므로 orElse를 보통 쓰게 됨.
        Employee foundEmp = employeeJpaRepository.findById(emp.getId()).orElse(null);

//        atl+enter : static import 자동완성
        assertThat(foundEmp).isNotNull()
//                .extracting("name")
//                :: 참조하는 것
//                람다식, 메소드 체이닝 무조건 공부해야 함!!
                .extracting(Employee::getName)
                .isEqualTo(emp.getName());
    }

    @Test
    void findByName() {
        employeeJpaRepository.save(emp);

        List<Employee> empList = employeeJpaRepository.findByName(emp.getName());

        assertThat(empList).isNotEmpty()
                .extracting("name")
                .containsExactly("홍길동");
    }

    @Test
    void findAll() {
        employeeJpaRepository.save(emp);

        List<Employee> empList = employeeJpaRepository.findAll();

        assertThat(empList).containsExactly(emp);
    }

    @AfterEach
    void tearDown() {
        em.remove(emp);
    }


}