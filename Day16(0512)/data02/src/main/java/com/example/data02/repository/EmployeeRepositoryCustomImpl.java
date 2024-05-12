package com.example.data02.repository;

import com.example.data02.entity.Employee;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.data02.entity.QEmployee.employee;

@RequiredArgsConstructor
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

//    ctrl + o : 오버라이드
    @Override
    public List<Employee> findAllDSL() {
        return queryFactory.selectFrom(employee)
                .fetch();
    }
}
