package com.rest.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.api.controller.BoardController;
import com.rest.api.domain.User;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.service.UserService;
import com.rest.api.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 시큐리티 같은 설정 타지않음 ,http://blog.devenjoy.com/?p=524
@Transactional
public class UserControllerTest {

    private Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final String headerNm = "Authorization";
    private String token;

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();

        Map<String,String> data = new HashMap<>();
        data.put("email","test@naver.com");
        data.put("password","1234");
        MvcResult result =  mockMvc.perform(
                MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk()).andReturn();

        given(result.getResponse().getHeader("Authorization"));
        this.token =  Optional.ofNullable(result.getResponse().getHeader("Authorization")).orElseThrow(() -> new NullPointerException("Authorization"));

    }

    @Test
    void postUsers() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .param("email", "test@naver.com")
                        .param("firstName", "first")
                        .param("lastName", "last")
                        .param("password", "1234"))
                .andExpect(status().isOk());
    }

    @Test
    void loginTest() throws Exception {

        Map<String,String> data = new HashMap<>();
        data.put("email","test@naver.com");
        data.put("password","1234");
        MvcResult result =  mockMvc.perform(
                MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk()).andReturn();

        given(result.getResponse().getHeader("Authorization"));
        this.token =  Optional.ofNullable(result.getResponse().getHeader("Authorization")).orElseThrow(() -> new NullPointerException("Authorization"));
    }

    @Test
    void getUsers() throws Exception {

        String CSRF_TOKEN = "";
        if (!StringUtils.isEmpty(this.token) && this.token.startsWith("Bearer ")){
            CSRF_TOKEN  = this.token.substring(7).trim();
        }

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1")
                        .header(headerNm,this.token)
                        .cookie(new Cookie("CSRF_TOKEN", CSRF_TOKEN)))
                .andExpect(status().isOk()).andReturn();

        System.out.println("#### result :: " + result.getResponse().getCookie("CSRF_TOKEN").getValue());

    }

}
