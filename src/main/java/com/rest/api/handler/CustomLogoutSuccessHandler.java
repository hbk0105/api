package com.rest.api.handler;

import com.google.gson.Gson;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.util.CookieUtils;
import com.rest.api.util.ResponseMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    // https://www.codejava.net/frameworks/spring-boot/spring-security-logout-success-handler-example

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        super.onLogoutSuccess(request, response, authentication);

        CookieUtils.deleteCookie(request,response, JwtTokenUtil.ACCESS_TOKEN_NAME);
        CookieUtils.deleteCookie(request,response,JwtTokenUtil.REFRESH_TOKEN_NAME);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        System.out.println("@@@ 타나요?");

        ResponseMessage ms = new ResponseMessage();
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.println( gson.toJson(ms));
        out.flush();
        out.close();

    }
}