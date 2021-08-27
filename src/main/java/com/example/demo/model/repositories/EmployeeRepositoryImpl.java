package com.example.demo.model.repositories;

import com.example.demo.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class EmployeeRepositoryImpl {

	@Autowired
	@Lazy
	EmployeeRepository employeeRepository;


	public void customMethod(Employee employee) {
		System.out.println("hola soy yo, el empleado: " + employee.getName());
	}
}
