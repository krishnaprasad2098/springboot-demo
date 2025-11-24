package com.employee_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.employee_management.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

    @Autowired
    private EmployeeService employeeService;

    // List all employees
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees"; // loads employees.html
    }

    // Show add employee form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new com.employee_management.model.Employee());
        return "add-employee";
    }

    // Handle form submit for new employee
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employee") com.employee_management.model.Employee employee) {
        employeeService.createEmployee(employee);
        return "redirect:/employees";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "edit-employee";
    }

    // Handle update
    @PostMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id,
            @ModelAttribute("employee") com.employee_management.model.Employee employee) {
        employeeService.updateEmployee(id, employee);
        return "redirect:/employees";
    }

    // Delete employee
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}
