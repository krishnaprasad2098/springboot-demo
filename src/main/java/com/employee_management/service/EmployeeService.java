package com.employee_management.service;

import java.util.List;

import com.employee_management.model.Employee;

public interface EmployeeService {
	
	Employee createEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);
}
