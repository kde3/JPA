package com.example.data02.repository;

import com.example.data02.entity.Department;
import com.example.data02.entity.QDepartment;
import com.example.data02.entity.QEmployee;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.data02.entity.QDepartment.department;
import static com.example.data02.entity.QEmployee.employee;

@Repository
public class DepartmentJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public DepartmentJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

//    <EntityManager로 만들 것>
//    저장
//    id로 조회

//    <DSL로 만들 것>
//    부서 이름으로 조회 - 1개
//    전화번호가 매개변수로 받은 값을 포함하는 부서 조회
//    부서에 소속된 사원이 n명 이상인 부서 조회

//    저장
    public void save(Department department) {
        em.persist(department);
    }

//    id로 조회
    public Optional<Department> findById(Long id) {
//        return Optional.ofNullable(
//                queryFactory.selectFrom(department)
//                .where(department.id.eq(id))
//                .fetchOne()
//        );

        return Optional.ofNullable(em.find(Department.class, id));
    }

    //부서 이름으로 조회
    public Optional<Department> findByName(String name) {
        return Optional.ofNullable(
                queryFactory.selectFrom(department)
                    .where(department.name.eq(name))
                    .fetchOne()
        );
    }

//    매개변수로 받은 전화번호 포함하는 부서 조회
    public List<Department> findByPhoneContaining(String phone) {
        return queryFactory.selectFrom(department)
                .where(department.phone.contains(phone))
                .fetch();
    }

//    부서에 소석된 사원이 n명 이상인 부서 조회
    public List<Department> findByEmployeeCountGoe(int n) {
        return queryFactory.selectFrom(department)
                .join(employee).on(department.eq(employee.department))
                .groupBy(department)
                .having(department.id.count().goe(n))
                .fetch();
    }

}
