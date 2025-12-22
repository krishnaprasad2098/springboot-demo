package com.employee_management.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.employee_management.impl.EmployeeServiceImpl;
import com.employee_management.model.Employee;
import com.employee_management.repository.EmployeeRepository;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmployeeById() {
        Employee emp = new Employee();
        emp.setId(1L);
        emp.setFirstName("Krishna");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        Employee result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals("Krishna", result.getFirstName());
    }
}
