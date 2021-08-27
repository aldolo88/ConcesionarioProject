package com.example.demo.model.repositories;

import com.example.demo.model.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	void customMethod(Employee employee);

	@Query("SELECT e from Employee e")
	List<Employee> findAllEmployees();


}