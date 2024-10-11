package com.example.rqchallenge;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@JsonProperty("employee_name")
    private String name;
	@JsonProperty("employee_salary")
    private Integer salary;
	@JsonProperty("employee_age")
    private String age;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

    public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
}
