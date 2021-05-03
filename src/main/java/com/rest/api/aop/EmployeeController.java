package com.rest.api.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aop")
public class EmployeeController {

    @Autowired
    private EmployeeService dummyService;

    // Get the employee detail based on employee id
    @GetMapping("/employee/{empid}")
    public Employee getEmployee(@PathVariable("empid") String empId) {
        return dummyService.getEmployee(empId);
    }
    //post the new employee details to backend
    @PostMapping("/employee")
    public String newEmployee(@RequestBody Employee employee) {
        return dummyService.addEmployee(employee);
    }
    // update the employee depart using emp id
    @PutMapping("/employee/{empid}")
    public String updateEmployee(@PathVariable("empid") String empId, @RequestParam("deptname") String deptName) {
        return dummyService.updateEmployeeDept(empId, deptName);
    }
    // Delete the employee
    @DeleteMapping("/employee/{empid}")
    public String deleteEmployee(@PathVariable("empid") String empId) {
        return dummyService.deleteEmployee(empId);
    }
}
