package com.rest.api.service;

import com.rest.api.domain.Role;
import com.rest.api.domain.User;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.repository.RoleRepository;
import com.rest.api.repository.UserRepository;
import com.rest.api.util.CookieUtils;
import com.rest.api.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public User singUp(User.Request userReqDto){
        User user = userRepository.findByEmail(userReqDto.getEmail());
        if(user == null){
            user = new User();
            user.setEmail(userReqDto.getEmail());
            user.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
            user.setFirstName(userReqDto.getFirstName());
            user.setLastName(userReqDto.getLastName());
            user.setEnabled(true);
            // https://java119.tistory.com/52
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime date  = startDate.plusMinutes(10);
            user.setMailCertificationtDate(date);

            Role role = roleRepository.findByName("ROLE_USER");
            user.setRoles(new ArrayList<>(Arrays.asList(role)));
            user = userRepository.save(user);

        }
        return user;
    }

    public Optional<User> findById(Long id){
        // Optional
        // https://advenoh.tistory.com/15

        return userRepository.findById(id);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User login(Map<String, String> data , ResponseMessage ms , HttpServletRequest req , HttpServletResponse res){
        User user = userRepository.findByEmail(data.get("email"));
        if(user == null)
            return null;
        String pw = data.get("password");
        String userPw = passwordEncoder.encode(data.get("password"));
        if(passwordEncoder.matches(pw,userPw)){
            ms.add("result",user);
            List<GrantedAuthority> roles = new ArrayList<>();
            for(Role r :  user.getRoles()){
                roles.add(new SimpleGrantedAuthority( r.getName()));
            }
            String username = user.getId()+"-"+user.getFirstName();
            UserDetails userDetails = userDetails(user.getEmail());
            String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(username);
            // https://velog.io/@ehdrms2034/Spring-Security-JWT-Redis%EB%A5%BC-%ED%86%B5%ED%95%9C-%ED%9A%8C%EC%9B%90%EC%9D%B8%EC%A6%9D%ED%97%88%EA%B0%80-%EA%B5%AC%ED%98%84
            CookieUtils.deleteCookie(req,res,JwtTokenUtil.ACCESS_TOKEN_NAME);
            CookieUtils.deleteCookie(req,res,JwtTokenUtil.REFRESH_TOKEN_NAME);
            CookieUtils.addCookie(res, JwtTokenUtil.ACCESS_TOKEN_NAME, accessToken , (int)JwtTokenUtil.JWT_ACCESS_TOKEN_VALIDITY);
            CookieUtils.addCookie(res,JwtTokenUtil.REFRESH_TOKEN_NAME, refreshToken , (int)JwtTokenUtil.JWT_REFRESH_TOKEN_VALIDITY);
            res.setHeader("Authorization","Bearer " + accessToken);

            //ms.add("accessToken","Bearer "+accessToken);
            // jwt  토큰 생성..
        }else{
            throw new IllegalArgumentException("IllegalArgumentException");
        }

       return  user;
    }

    public UserDetails userDetails(String email){
        User user = userRepository.findByEmail(email);
        if(user == null)
            throw new NoResultException("사용자가 존재하지 않습니다.");
        List<GrantedAuthority> roles = new ArrayList<>();
        for(Role r :  user.getRoles()){
            roles.add(new SimpleGrantedAuthority( r.getName()));
        }
        String username = user.getId()+"-"+user.getFirstName();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), roles);
        return userDetails;
    }

    public void save(User user){
        userRepository.save(user);
    }

}
