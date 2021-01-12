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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
   //  https://webfirewood.tistory.com/115
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody Map<String, String> user) {
        return new ResponseEntity<>(userRepository.save(User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()), HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/login")
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