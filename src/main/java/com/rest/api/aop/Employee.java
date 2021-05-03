package com.rest.api.aop;

import lombok.Data;

/*
# AOP
https://velog.io/@ayoung0073/springboot-AOP
https://budnamu.tistory.com/entry/%EC%9E%91%EC%84%B1%EC%A4%91-Spring-Boot-and-Spring-AOP-Aspect-Oriented-Programming
https://myweblearner.com/springboot_2_aop?fbclid=IwAR3EGtIE2ig2QqvCErXp_Hg26V0HYgM0A18EDdCshE2-FvCicQx5FM6aIsg
https://hirlawldo.tistory.com/31
*/
@Data
public class Employee {
    private String empId;
    private String empName;
    private String deptName;
}
