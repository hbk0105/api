package com.rest.api.exception;

import com.google.gson.Gson;
import com.rest.api.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class RestAccessDeniedExceptionHandler implements AccessDeniedHandler {

    private Logger log = LoggerFactory.getLogger(RestAccessDeniedExceptionHandler.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        log.info("RestAccessDeniedExceptionHandler !!!!!!!!!!!!! ");
        ResponseMessage ms = new ResponseMessage(HttpStatus.FORBIDDEN, "Access Denied", httpServletRequest.getRequestURL().toString());
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        Gson gson = new Gson();
        out.println( gson.toJson(ms));
        out.flush();
        out.close();
    }
}