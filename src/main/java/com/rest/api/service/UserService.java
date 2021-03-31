package com.rest.api.service;

import com.rest.api.config.RedisConfig;
import com.rest.api.domain.*;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.repository.*;
import com.rest.api.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PrivilegesRepository privilegesRepository;

    @Transactional
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
           // user.setRoles(new ArrayList<>(Arrays.asList(role)));
            user = userRepository.save(user);

            UserRoles userRole = new UserRoles();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleRepository.save(userRole);

            final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
            final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
            final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege));

            for(int i = 0; i < userPrivileges.size(); i++){
                Privileges privileges = new Privileges();
                privileges.setRole(role);
                privileges.setPrivilege(userPrivileges.get(i));
                privilegesRepository.save(privileges);
            }


        }
        return user;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    public Optional<User> findById(Long id){
        // Optional
        // https://advenoh.tistory.com/15

        return userRepository.findById(id);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User login(Map<String, String> data , HttpServletRequest req , HttpServletResponse res){
        User user = userRepository.findByEmail(data.get("email"));
        if(user == null)
            return null;
        String pw = data.get("password");
        String userPw = passwordEncoder.encode(data.get("password"));
        if(passwordEncoder.matches(pw,userPw)){
            List<GrantedAuthority> roles = new ArrayList<>();
            for(UserRoles r :  user.getRoles()){
                roles.add(new SimpleGrantedAuthority(r.getRole().getName()));
            }
            String username =  user.getUser_id()+"-"+user.getFirstName();
            UserDetails userDetails = userDetails(user.getEmail());
            String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(username);
            // https://velog.io/@ehdrms2034/Spring-Security-JWT-Redis%EB%A5%BC-%ED%86%B5%ED%95%9C-%ED%9A%8C%EC%9B%90%EC%9D%B8%EC%A6%9D%ED%97%88%EA%B0%80-%EA%B5%AC%ED%98%84
            CookieUtils.deleteCookie(req,res,JwtTokenUtil.ACCESS_TOKEN_NAME);
            CookieUtils.deleteCookie(req,res,JwtTokenUtil.REFRESH_TOKEN_NAME);
            CookieUtils.addCookie(res, JwtTokenUtil.ACCESS_TOKEN_NAME, accessToken , (int)JwtTokenUtil.JWT_ACCESS_TOKEN_VALIDITY);
            CookieUtils.addCookie(res,JwtTokenUtil.REFRESH_TOKEN_NAME, refreshToken , (int)JwtTokenUtil.JWT_REFRESH_TOKEN_VALIDITY);

            // header - Authorization 사용시 redis를 사용, 현재 프로젝트는 jwt를 쿠키에 저장
            /*
            res.setHeader("Authorization","Bearer " + accessToken);
            Token retok = new Token();
            retok.setUsername(username);
            retok.setRefreshToken(accessToken);
            ValueOperations<String, Object> vop = redisTemplate.opsForValue();
            vop.set(username, retok);
            */
            // jwt  토큰 생성..
        }else{
            throw new IllegalArgumentException("IllegalArgumentException");
        }

       return  user;
    }



    @Transactional // @Transactional 설정 이유 : https://www.inflearn.com/questions/33949
    public UserDetails userDetails(String email){
        User user = userRepository.findByEmail(email);
        if(user == null)
            throw new NoResultException("사용자가 존재하지 않습니다.");
        List<GrantedAuthority> roles = new ArrayList<>();
        for(UserRoles r :  user.getRoles()){
            roles.add(new SimpleGrantedAuthority(r.getRole().getName()));
        }
        String username = user.getUser_id()+"-"+user.getFirstName();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), roles);
        return userDetails;
    }

    public void save(User user){
        userRepository.save(user);
    }

}
