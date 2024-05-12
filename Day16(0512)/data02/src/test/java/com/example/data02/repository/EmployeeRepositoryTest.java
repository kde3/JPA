package com.example.data02.repository;

import com.example.data02.entity.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class EmployeeRepositoryTest {
    @Autowired EmployeeRepository employeeRepository;

    @Test
    @DisplayName("test")
    void test() {
        //given
        employeeRepository.save(Employee.builder().name("홍길동").build());
        //when

        List<Employee> list = employeeRepository.findAllDSL();
        //then

        Assertions.assertThat(list).hasSize(1);
    }
}