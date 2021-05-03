package com.rest.api.aop;

public interface EmployeeService {
    public Employee getEmployee(String empId);
    public String addEmployee(Employee e);
    public String updateEmployeeDept(String empId,String deptName);
    public String deleteEmployee(String empId);
}