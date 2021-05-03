package com.rest.api.aop;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class EmployeeServiceImpl implements EmployeeService {

    private static List<Employee> employeeLst = new ArrayList<>();

    @Override
    @CalculatePerformance
    public Employee getEmployee(String empId) {
        Stream<Employee> empStream = employeeLst.stream().filter(emp -> {
            return emp.getEmpId().equalsIgnoreCase(empId);
        });
        sleepForSeconds(5000L);
        return empStream.findAny().get();
    }

    @Override
    @MethodLogger
    public String addEmployee(Employee e) {
        employeeLst.add(e);
        sleepForSeconds(3000L);
        return "Success";
    }

    @Override
    @MethodLogger
    public String updateEmployeeDept(String empId, String deptName) {
        employeeLst.stream().forEach(emp -> {
            if (emp.getEmpId().equalsIgnoreCase(empId)) {
                emp.setDeptName(deptName);
            }
        });
        sleepForSeconds(2000L);

        return "SUCCESS";
    }

    @Override
    @MethodLogger
    public String deleteEmployee(String empId) {
        System.out.println("Employee Id -->" + empId);
        if (employeeLst.removeIf(emp -> emp.getEmpId().equalsIgnoreCase(empId)))
            return "SUCCESS";
        else
            return "FAIURE";
    }

    public void sleepForSeconds(Long period) {
        try {
            Thread.sleep(period);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        Employee emp1 = new Employee();
        Employee emp2 = new Employee();
        // employee 1
        emp1.setEmpId("A1234");
        emp1.setEmpName("Sam");
        emp1.setDeptName("IT");
        // employee 2
        emp2.setEmpId("B1234");
        emp2.setEmpName("Tom");
        emp2.setDeptName("Finance");

        employeeLst.add(emp1);
        employeeLst.add(emp2);
    }

}