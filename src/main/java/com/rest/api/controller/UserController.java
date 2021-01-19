package com.rest.api.controller;

import com.rest.api.domain.User;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.repository.UserQueryRepository;
import com.rest.api.service.UserService;
import com.rest.api.util.MailUtil;
import com.rest.api.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
    // https://webfirewood.tistory.com/115
    // https://velog.io/@ehdrms2034/Spring-Security-JWT-Redis%EB%A5%BC-%ED%86%B5%ED%95%9C-%ED%9A%8C%EC%9B%90%EC%9D%B8%EC%A6%9D%ED%97%88%EA%B0%80-%EA%B5%AC%ED%98%84-4-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85-%EC%9D%B8%EC%A6%9D-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%95%84%EC%9D%B4%EB%94%94-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EC%B0%BE%EA%B8%B0
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/signUp")
    public ResponseEntity<ResponseMessage> join(User.Request user) throws UnsupportedEncodingException, MessagingException {

        ResponseMessage ms = new ResponseMessage();
        try {
            userService.singUp(user);
            if(MailUtil.signCertificationMail("test","마이클",user.getEmail(),user.getLastName() , javaMailSender)){
                ms.setMessage("SUCCESS");
                ms.setStatus(ResponseMessage.StatusEnum.OK);
                ms.setData("본인 확인 메일이 발송 되었습니다.");
            }else{
                ms.setMessage("시스템에 일시적인 오류가 발생하였습니다.");
                ms.setStatus(ResponseMessage.StatusEnum.INTERNAL_SERER_ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(ms, HttpStatus.OK);
        // return new ResponseEntity<>();
    }


    @GetMapping("/signUp/completed/{email}")
    public ResponseEntity<ResponseMessage> completed(@PathVariable String email) {

        ResponseMessage ms = new ResponseMessage();
        User user = userService.findByEmail(email);

        if(user != null){

            LocalDateTime  nowDate = LocalDateTime.now();
            LocalDateTime endDate  =  user.getMailCertificationtDate();

            // startTime이 endTime 보다 이전 시간 인지 비교
            if(nowDate.isBefore(endDate)){
                user.setMailCertification(true);
                userQueryRepository.update(user);
                ms.setMessage("SUCCESS");
                ms.setStatus(ResponseMessage.StatusEnum.OK);
            }else{
                ms.setMessage("Authentication Timeout");
                ms.setStatus(ResponseMessage.StatusEnum.INTERNAL_SERER_ERROR);
            }

        }else{
            ms.setMessage("FAIL");
            ms.setStatus(ResponseMessage.StatusEnum.INTERNAL_SERER_ERROR);
        }

        return new ResponseEntity<>(ms, HttpStatus.OK);
    }


    @PostMapping("/get/user/{id}")
    public ResponseEntity<ResponseMessage> getUser(@PathVariable Long id){
        ResponseMessage ms = new ResponseMessage();
        ms.setMessage("SUCCESS");
        ms.setStatus(ResponseMessage.StatusEnum.OK);
        ms.setData(userService.findById(id));
        return new ResponseEntity<>(ms, HttpStatus.OK);
    }

    // 로그인
   /* @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        User member = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        org.springframework.security.core.userdetails.UserDetails userDetails =
               new org.springframework.security.core.userdetails.User(member.getUsername() , member.getPassword() , member.getAuthorities());

        return jwtTokenProvider.generateAccessToken(userDetails);
    }
*/
    // 로그인
   /* @PostMapping("/{email}")
    public ResponseEntity user( @PathVariable String email) {
        return new ResponseEntity<>(userRepository.findByEmail(email), HttpStatus.OK);
    }

*/
}