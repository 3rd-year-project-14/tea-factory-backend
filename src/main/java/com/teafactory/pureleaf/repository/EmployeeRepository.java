package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>  {
}
