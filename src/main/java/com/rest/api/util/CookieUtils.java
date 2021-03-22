package com.rest.api.util;

import com.rest.api.controller.BoardController;
import com.rest.api.jwt.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.SerializationUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 *
 * Description : 쿠키 클래스
 *
 * Modification Information
 * 수정일			 수정자						수정내용
 * -----------	-----------------------------  -------
 * 2021. 3.  22.    MICHAEL						최초작성
 *
 */
public class CookieUtils {

    private static Logger logger = LoggerFactory.getLogger(BoardController.class);

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())));
    }


    public static void logout(JwtTokenUtil jwtTokenUtil ,  RedisTemplate<String, Object> redisTemplate
            , HttpServletRequest req , HttpServletResponse res) throws Exception{

        String username = null;
        Cookie cookie  = null;
        String accessToken = "";
        try {

            if(CookieUtils.getCookie(req,jwtTokenUtil.ACCESS_TOKEN_NAME).isPresent()){
                cookie = CookieUtils.getCookie(req,jwtTokenUtil.ACCESS_TOKEN_NAME).get();
            }

            if(cookie != null){
                accessToken = cookie.getValue();
                if(jwtTokenUtil.getUsername(accessToken) != null){
                    username = jwtTokenUtil.getUsername(accessToken);
                }
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ExpiredJwtException e) { //expire됐을 때
            e.printStackTrace();
            username = e.getClaims().getSubject();
            logger.info("username from expired access token: " + username);
        } catch (NullPointerException e) {
            logger.info("access token Expired");
            cookie = null;
            if(CookieUtils.getCookie(req,jwtTokenUtil.REFRESH_TOKEN_NAME).isPresent()){
                cookie = CookieUtils.getCookie(req,jwtTokenUtil.REFRESH_TOKEN_NAME).get();
            }
            if(cookie != null){
                accessToken = cookie.getValue();
                if(jwtTokenUtil.getUsername(accessToken) != null){
                    username = jwtTokenUtil.getUsername(accessToken);
                }
            }
        }   catch (Exception e) {
            e.printStackTrace();
        }

        /*
        try {

            if (redisTemplate.opsForValue().get(username) != null) {
                //delete refresh token
                redisTemplate.delete(username);
            }
        } catch (IllegalArgumentException e) {
            logger.warn("user does not exist");
        }

       // header - Authorization 사용시 redis를 사용, 현재 프로젝트는 jwt를 쿠키에 저장

        logger.info(" logout ing accessToken :::  : " + accessToken);
        //cache logout token for 10 minutes!
        if(accessToken != ""){
            redisTemplate.opsForValue().set(accessToken, true);
            redisTemplate.expire(accessToken, JwtTokenUtil.JWT_ACCESS_TOKEN_VALIDITY, TimeUnit.MILLISECONDS);
        }
        */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(req, res, auth);
        }

    }

}
