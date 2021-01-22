package com.rest.api.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rest.api.domain.Token;
import com.rest.api.jwt.JwtTokenUtil;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
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
        String requestTokenHeader = request.getHeader("Authorization");
        logger.info("### requestTokenHeader :: " + requestTokenHeader);
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7).trim();


            try {

                if(jwtTokenUtil.validateToken(jwtToken)){ // 만료 체크
                    username = jwtTokenUtil.getUsername(jwtToken);
                    System.out.println("#### username :: " + username);
                }else{
                    // 만료
                    logger.info("### username :: " + username);
                }

            } catch (IllegalArgumentException e) {
                logger.warn("Unable to get JWT Token");
            }
            catch (ExpiredJwtException e) {
                logger.warn("Expired  JWT Token");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (username == null) {
            logger.info("token maybe expired: username is null.");
       /* } else if (redisTemplate.opsForValue().get(username) != null) {
            logger.warn("this token already logout!");*/
        } else {

            /*
            DecodedJWT jwt = JWT.decode(jwtToken);
            logger.info("########################### " +jwt.getSubject());

            String decodeJwtSubject = jwt.getSubject();

            // 토큰 값 변환 -> 리프레시 토큰 값 주기..
            ValueOperations<String, Object> vop2 = redisTemplate.opsForValue();
            Token result = (Token) vop2.get(decodeJwtSubject); // 유저 이름으로 redis에서 리프레시 토큰값 추출.\

            logger.info("@#@#@ result :: " + result);

            */

            // 토큰 값 변환

            //만든 authentication 객체로 매번 인증받기
            /*
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                    UsernamePasswordAuthenticationToken(userDetails , null ,userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            */

            // 로그아웃 ..
            //https://www.programcreek.com/java-api-examples/?api=org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler

        }

        XssAndSqlHttpServletRequestWrapper xssRequestWrapper = new XssAndSqlHttpServletRequestWrapper(request);
        chain.doFilter(xssRequestWrapper, response);
    }
}
