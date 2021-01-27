package com.rest.api.exception;

import com.google.gson.Gson;
import com.rest.api.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Configuration
public class RestAuthenticationExceptionHandler implements AuthenticationEntryPoint {

    private Logger log = LoggerFactory.getLogger(RestAuthenticationExceptionHandler.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("RestAuthenticationExceptionHandler !!!!!!!!!!!!! ");
        ResponseMessage ms = new ResponseMessage(HttpStatus.UNAUTHORIZED, "Unauthorized", httpServletRequest.getRequestURL().toString());
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        Gson gson = new Gson();
        out.println( gson.toJson(ms));
        out.flush();
        out.close();
    }
}
