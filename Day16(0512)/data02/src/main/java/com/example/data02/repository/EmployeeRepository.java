package com.example.data02.repository;

import com.example.data02.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//?? extends 여러개?
//JPQL이나 queryDSL을 사용하고 싶을 때 이렇게 하면 됨.
public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeRepositoryCustom {

}
