package com.rest.api.controller;

import com.rest.api.domain.Role;
import com.rest.api.domain.User;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.repository.UserQueryRepository;
import com.rest.api.service.UserService;
import com.rest.api.util.CookieUtils;
import com.rest.api.util.MailUtil;
import com.rest.api.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {
    // https://webfirewood.tistory.com/115
    // https://velog.io/@ehdrms2034/Spring-Security-JWT-Redis%EB%A5%BC-%ED%86%B5%ED%95%9C-%ED%9A%8C%EC%9B%90%EC%9D%B8%EC%A6%9D%ED%97%88%EA%B0%80-%EA%B5%AC%ED%98%84-4-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85-%EC%9D%B8%EC%A6%9D-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%95%84%EC%9D%B4%EB%94%94-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EC%B0%BE%EA%B8%B0
    private final JwtTokenUtil jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // ERROR 테스트
    @GetMapping("/api/error")
    public ResponseMessage error(HttpServletRequest req) throws Exception {
        throw new Exception("Exception");
        //throw new RuntimeException("RuntimeException");
    }

    @PostMapping("/users")
    public ResponseMessage signUp(User.Request user) throws RuntimeException{
        ResponseMessage ms = new ResponseMessage();
        try {
            userService.singUp(user);
            MailUtil.signCertificationMail("test","마이클",user.getEmail(),user.getLastName() , javaMailSender);
            ms.setMessage("본인 확인 메일이 발송 되었습니다");
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return ms;
    }

    @GetMapping("/users/completed/{email}")
    public ResponseMessage completed(@PathVariable String email , HttpServletRequest req) {
        ResponseMessage ms = null;
        User user = userService.findByEmail(email);
        // null 일 경우
        if(user == null)  return ms = new ResponseMessage(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.", req.getRequestURL().toString());
        LocalDateTime nowDate = LocalDateTime.now();
        if(user.getMailCertificationtDate() == null) return ms = new ResponseMessage(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.", req.getRequestURL().toString());
        LocalDateTime endDate  =  user.getMailCertificationtDate();
        // startTime이 endTime 보다 이전 시간 인지 비교
        if(nowDate.isBefore(endDate)){
            user.setMailCertification(true);
            userQueryRepository.update(user);
            ms = new ResponseMessage();
        }else{
            ms = new ResponseMessage(HttpStatus.BAD_REQUEST, "Authentication Timeout", req.getRequestURL().toString());
        }
        return ms;
    }


    @GetMapping("/users/{id}")
    public ResponseMessage users(@PathVariable Long id , HttpServletRequest req) throws Throwable {
        Optional<User> user  = Optional.ofNullable(userService.findById(id).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다.")));
        ResponseMessage ms = new ResponseMessage();
        ms.add("result",user.get());
        return ms;
    }

    @PostMapping("/login")
    public ResponseMessage login(@RequestBody Map<String, String> data , HttpServletRequest req , HttpServletResponse res){
        ResponseMessage ms = new ResponseMessage();
        if("".equals(data.get("email")) || "".equals(data.get("password")))
            return ms = new ResponseMessage(HttpStatus.BAD_REQUEST, "IllegalArgumentException", req.getRequestURL().toString());
        User user = userService.login(data , ms, req , res);
        if(user == null)
            return ms = new ResponseMessage(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.", req.getRequestURL().toString());

        return ms;
    }


}