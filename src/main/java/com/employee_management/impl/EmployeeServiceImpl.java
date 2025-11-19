package com.employee_management.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee_management.model.Employee;
import com.employee_management.repository.EmployeeRepository;
import com.employee_management.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Employee createEmployee(Employee employee) {
		 return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> getAllEmployees() {
		
		return employeeRepository.findAll();
	}

	@Override
	public Employee getEmployeeById(Long id) {
		 Optional<Employee> optional = employeeRepository.findById(id);
		return optional.orElse(null);
	}

	@Override
	public Employee updateEmployee(Long id, Employee employeeDetails) {
		 Optional<Employee> optional = employeeRepository.findById(id);

	        if (optional.isPresent()) {
	            Employee existing = optional.get();

	            existing.setFirstName(employeeDetails.getFirstName());
	            existing.setLastName(employeeDetails.getLastName());
	            existing.setEmail(employeeDetails.getEmail());
	            existing.setDepartment(employeeDetails.getDepartment());
	            existing.setDesignation(employeeDetails.getDesignation());
	            existing.setSalary(employeeDetails.getSalary());
	            existing.setPhone(employeeDetails.getPhone());
	            existing.setDateOfJoining(employeeDetails.getDateOfJoining());
	            existing.setStatus(employeeDetails.getStatus());

	            return employeeRepository.save(existing);
	        }
	        return null;
	}

	@Override
	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}

}
