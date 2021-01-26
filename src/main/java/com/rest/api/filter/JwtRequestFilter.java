package com.rest.api.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rest.api.domain.Token;
import com.rest.api.domain.User;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.service.UserService;
import com.rest.api.util.CookieUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
/*
   public Authentication getAuthentication(String token) {
        Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);

        List<String> rs =(List)parseInfo.get("role");
        Collection<GrantedAuthority> tmp= new ArrayList<>();
        for (String a: rs) {
            tmp.add(new SimpleGrantedAuthority(a));
        }
        UserDetails userDetails = User.builder().username(String.valueOf(parseInfo.get("username"))).authorities(tmp).password("asd").build();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        return usernamePasswordAuthenticationToken;
    }*/

    @Bean
    public FilterRegistrationBean JwtRequestFilterRegistration (JwtRequestFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // https://do-study.tistory.com/106
        // https://velog.io/@ehdrms2034/Spring-Security-JWT-Redis%EB%A5%BC-%ED%86%B5%ED%95%9C-%ED%9A%8C%EC%9B%90%EC%9D%B8%EC%A6%9D%ED%97%88%EA%B0%80-%EA%B5%AC%ED%98%84

        String username = null;
        String jwtToken = null;
        String refToken = null;
        Cookie cookie = null;

        String requestTokenHeader = request.getHeader("Authorization");
        logger.info("### requestTokenHeader :: " + requestTokenHeader);
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7).trim();
            try {

                username = jwtTokenUtil.getUsername(jwtToken);
                if(username != null){
                    String r[] = username.split("-");
                    Long id = Long.parseLong(r[0]);
                    setAuthentication(request , response , id,false);
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
                logger.error("Expired  JWT Token");
                cookie = null;
                if(CookieUtils.getCookie(request,JwtTokenUtil.REFRESH_TOKEN_NAME).isPresent()){
                    cookie = CookieUtils.getCookie(request,jwtTokenUtil.REFRESH_TOKEN_NAME).get();
                }
                if(cookie != null){
                    refToken = cookie.getValue();
                }
            }
        } else {
            logger.error("JWT Token does not begin with Bearer String");
        }

        if(refToken != null){
            try {
                username = jwtTokenUtil.getUsername(refToken);
                if(username != null){
                    String r[] = username.split("-");
                    Long id = Long.parseLong(r[0]);
                    setAuthentication(request , response , id,true);
                }

            } catch (ExpiredJwtException e) {
                e.printStackTrace();
                logger.error("Expired  JWT Token");
            }
        }

        XssAndSqlHttpServletRequestWrapper xssRequestWrapper = new XssAndSqlHttpServletRequestWrapper(request);
        chain.doFilter(xssRequestWrapper, response);
    }

    public void setAuthentication(HttpServletRequest request ,  HttpServletResponse response , Long id , boolean ref){

        Optional<User> user  = Optional.ofNullable(userService.findById(id).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다.")));
        UserDetails userDetails = userService.userDetails(user.get().getEmail());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                UsernamePasswordAuthenticationToken(userDetails , null ,userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        if(ref){
            userDetails = userService.userDetails(user.get().getEmail());
            String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
            CookieUtils.deleteCookie(request , response , JwtTokenUtil.ACCESS_TOKEN_NAME);
            CookieUtils.addCookie(response , JwtTokenUtil.ACCESS_TOKEN_NAME , accessToken , (int)JwtTokenUtil.JWT_ACCESS_TOKEN_VALIDITY);
            response.setHeader("Authorization","Bearer " + accessToken);
        }

    }
}
