package com.rest.api.controller;

import com.rest.api.domain.Role;
import com.rest.api.domain.User;
import com.rest.api.domain.UserRoles;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.repository.UserQueryRepository;
import com.rest.api.service.UserService;
import com.rest.api.util.CookieUtils;
import com.rest.api.util.MailUtil;
import com.rest.api.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.naming.AuthenticationException;
import javax.persistence.NoResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * Description : 사용자 컨트롤러
 *
 * Modification Information
 * 수정일			 수정자						수정내용
 * -----------	-----------------------------  -------
 * 2021. 3.  22.    MICHAEL						최초작성
 *
 */
@RequiredArgsConstructor
@RestController
public class UserController {
    // https://webfirewood.tistory.com/115
    // https://velog.io/@ehdrms2034/Spring-Security-JWT-Redis%EB%A5%BC-%ED%86%B5%ED%95%9C-%ED%9A%8C%EC%9B%90%EC%9D%B8%EC%A6%9D%ED%97%88%EA%B0%80-%EA%B5%AC%ED%98%84-4-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85-%EC%9D%B8%EC%A6%9D-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%95%84%EC%9D%B4%EB%94%94-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EC%B0%BE%EA%B8%B0
    private final JwtTokenUtil jwtTokenProvider;

    private Logger logger = LoggerFactory.getLogger(BoardController.class);

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

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 에러 테스트
     * @param req
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 에러 테스트
    @GetMapping("/api/error")
    public ResponseMessage error(HttpServletRequest req) throws Exception {
        throw new Exception("Exception");
        //throw new RuntimeException("RuntimeException");
    }

    /**
     * 사용자 등록
     * @param user
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 사용자 등록
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

    /**
     * 이메일 본인 인증
     * @param email
     * @param req
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 이메일 본인 인증
    @GetMapping("/completed/{email}")
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

    /**
     * 사용자 조회
     * @param id
     * @param req
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 사용자 조회
    @GetMapping("/users/{id}")
    public ResponseMessage users(@PathVariable Long id , HttpServletRequest req) throws Throwable {
        ResponseMessage ms = new ResponseMessage();
        Optional<User> user  = Optional.ofNullable(userService.findById(id).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다.")));
        if(!accessAuthCheck(user.get(), id , req)) throw new AuthenticationException("Unauthorized");

        Collection<Role.Response> role =  new ArrayList<>();
        user.get().getRoles().forEach((k) ->{
            Role.Response r = Role.Response.builder().name(k.getRole().getName()).build();
            role.add(r);
        });

        ms.add("result",User.Response.builder()
                .email(user.get().getEmail())
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .roles(role).build());
        return ms;
    }

    /**
     * 권한 체크
     * @param user
     * @param userId
     * @param req
     * @return boolean
     * @throws Exception
     */
    // TODO: 권한 체크
    public boolean accessAuthCheck(User user ,  Long userId , HttpServletRequest req) throws Exception {
        //String requestTokenHeader = req.getHeader("Authorization");
        //String jwtToken = requestTokenHeader.substring(7).trim();

        Cookie ckToken = null;
        if(CookieUtils.getCookie(req,jwtTokenUtil.ACCESS_TOKEN_NAME).isPresent()){
            ckToken = CookieUtils.getCookie(req,jwtTokenUtil.ACCESS_TOKEN_NAME).get();
        }
        String requestTokenHeader = "";
        if(ckToken != null){
            requestTokenHeader = ckToken.getValue();
        }

        String username = jwtTokenUtil.getUsername(requestTokenHeader);
        if(username != null){
            String r[] = username.split("-");
            Long id = Long.parseLong(r[0]);
            if(id == userId){
                return true;
            }else{
                Optional<User> tokenUser  = Optional.ofNullable(userService.findById(id).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다.")));

                // https://woowacourse.github.io/javable/post/2020-05-14-foreach-vs-forloop/
                /*
                tokenUser.get().getRoles().forEach((k) ->{
                    if("ROLE_ADMIN".equals(k.getName()))  return true; return@loop;
                });*/

                for(UserRoles role :  tokenUser.get().getRoles()){
                    if("ROLE_ADMIN".equals(role.getRole().getName())) return true;
                }
            }
        }
        return false;
    }

    /**
     * 로그인
     * @param data
     * @param req
     * @param res
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 로그인
    @PostMapping("/api/login")
    public ResponseMessage login(@RequestBody Map<String, String> data , HttpServletRequest req , HttpServletResponse res){
        ResponseMessage ms = new ResponseMessage();
        if("".equals(data.get("email")) || "".equals(data.get("password")))
            return ms = new ResponseMessage(HttpStatus.BAD_REQUEST, "IllegalArgumentException", req.getRequestURL().toString());
        User user = userService.login(data , req , res);
        if(user == null)
            return ms = new ResponseMessage(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.", req.getRequestURL().toString());
        return ms;
    }

    /**
     * 로그아웃
     * @param req
     * @param res
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 로그아웃
    @GetMapping(value="/api/logout")
    public ResponseMessage logout(HttpServletRequest req , HttpServletResponse res) throws Exception {
        ResponseMessage ms = new ResponseMessage();
        CookieUtils.logout(jwtTokenUtil,redisTemplate,req,res);
        CookieUtils.deleteCookie(req,res, JwtTokenUtil.ACCESS_TOKEN_NAME);
        CookieUtils.deleteCookie(req,res, JwtTokenUtil.REFRESH_TOKEN_NAME);
        return ms;
    }

}