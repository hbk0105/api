package com.rest.api;

import com.rest.api.util.PasswordEncoding;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class ApiApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 테스트(){

        LocalDateTime  currentTime = LocalDateTime.now();
        String formatChangeDay = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("포맷 변경 : " + formatChangeDay);

        LocalDateTime test  = currentTime.plusMinutes(10);
        String formatChangeDay2 = test.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("포맷 변경2 : " + formatChangeDay2);

        // startTime이 endTime 보다 이전 시간 인지 비교
        System.out.println("startTime이 endTime 보다 이전 시간 인지 비교" + currentTime.isBefore(test));    // tru

        // startTime이 endTime 보다 이후 시간 인지 비교
        System.out.println("startTime이 endTime 보다 이후 시간 인지 비교 " +  currentTime.isAfter(test)); // false

    }

    @Test
    public void 암호화_테스트(){

        PasswordEncoding passwordEncoding = new PasswordEncoding();

        String rawPassword1 = "12345678";
        String rawPassword2 = "12345678";

        String newPassword1 = passwordEncoding.encode(rawPassword1);
        String newPassword2 = passwordEncoding.encode(rawPassword2);

        System.out.println("newPassword1 : "+newPassword1);
        System.out.println("newPassword2 : "+newPassword2);

        System.out.println("boolean : "+newPassword1==newPassword2);
        System.out.println("matches : "+passwordEncoding.matches(rawPassword2,newPassword1));
    }
}

