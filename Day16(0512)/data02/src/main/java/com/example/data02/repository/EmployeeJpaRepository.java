package com.example.data02.repository;

import com.example.data02.entity.Employee;
import com.example.data02.entity.QEmployee;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static com.example.data02.entity.QEmployee.employee;

@Repository
public class EmployeeJpaRepository {
    private final EntityManager em;

//표준이 아니기 때문에 bean 등록이 안되어 있음.
//    직접 생성자 만들어서 주입해줘야 함.
    private final JPAQueryFactory queryFactory;

    public EmployeeJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void save(Employee employee) {
        em.persist(employee);
    }

    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.select(employee)
                        .where(employee.id.eq(id))
                        .fetchOne()
        );
    }

    public List<Employee> findByName(String name) {
        return queryFactory.selectFrom(employee)
                .where(employee.name.eq(name))
                .fetch();
    }

    public List<Employee> findAll() {
        return queryFactory.selectFrom(employee)
                .fetch();
    }
}
