package com.example.demo.model;

import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name="employees")
public class Employee {

	@Id @GeneratedValue private long id;
	private String name;
	private String role;
	//    @OneToMany(mappedBy = "employee") private List<Sale> sales;
	//    @OneToMany(mappedBy = "employee") private List<Order> orders;

	Employee() {}

	public Employee(String name, String role) {

		this.name = name;
		this.role = role;
	}

	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getRole() {
		return this.role;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof Employee))
			return false;
		Employee employee = (Employee) o;
		return Objects.equals(this.id, employee.id) && Objects.equals(this.name, employee.name)
			&& Objects.equals(this.role, employee.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.name, this.role);
	}

	@Override
	public String toString() {
		return "Employee{" + "id=" + this.id + ", name='" + this.name + '\'' + ", role='" + this.role + '\'' + '}';
	}
}
