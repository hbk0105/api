package com.rest.api.controller;

import com.rest.api.domain.User;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.repository.UserRepository;
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
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
    // https://webfirewood.tistory.com/115
    // https://velog.io/@ehdrms2034/Spring-Security-JWT-Redis%EB%A5%BC-%ED%86%B5%ED%95%9C-%ED%9A%8C%EC%9B%90%EC%9D%B8%EC%A6%9D%ED%97%88%EA%B0%80-%EA%B5%AC%ED%98%84-4-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85-%EC%9D%B8%EC%A6%9D-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%95%84%EC%9D%B4%EB%94%94-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EC%B0%BE%EA%B8%B0
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
   /* @PostMapping("/join")
    public ResponseEntity join(@RequestBody Map<String, String> user) {
        return new ResponseEntity<>(userRepository.save(User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()), HttpStatus.OK);
    }*/

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
    @PostMapping("/{email}")
    public ResponseEntity user( @PathVariable String email) {
        return new ResponseEntity<>(userRepository.findByEmail(email), HttpStatus.OK);
    }


    // https://hhseong.tistory.com/167
    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/")
    public String index() throws MessagingException, UnsupportedEncodingException {

        String to = "michael@sangs.co.kr";
        String from = "hshan@test.com";
        String subject = "이메일 테스트!";

        StringBuilder body = new StringBuilder();
        body.append("<html> <body><h1>Hello </h1>");
        body.append("<div>테스트 입니다2.</div> </body></html>");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

        mimeMessageHelper.setFrom(new InternetAddress("kingsman@naver.com", "마이클"));
        mimeMessageHelper.setTo(new InternetAddress(to, "미키"));
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body.toString(), true);

        FileSystemResource fileSystemResource = new FileSystemResource(new File("C:\\Users\\sangs\\Desktop\\JJAL\\도커.txt"));
        mimeMessageHelper.addAttachment("도커.txt", fileSystemResource);

        FileSystemResource file = new FileSystemResource(new File("C:\\Users\\sangs\\Desktop\\JJAL\\joker.jpg"));
        mimeMessageHelper.addInline("joker.jpg", file);

        javaMailSender.send(message);

        return "하이";
    }

}