package com.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 시큐리티 같은 설정 타지않음 ,http://blog.devenjoy.com/?p=524
@Transactional
public class BoardControllerTest {

    private Logger log = LoggerFactory.getLogger(BoardControllerTest.class);

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
    void getBoardList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/boards"))
                .andExpect(status().isOk());
    }

    @Test
    void getBoradOne() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/boards/1"))
                .andExpect(status().isOk());
    }

    @Test
    void postBoard() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/boards")
                        .param("title", "제목입니다.")
                        .param("content", "내용입니다.")
                        .header(headerNm,this.token)
                        .cookie(new Cookie("CSRF_TOKEN", csrfToken())))
                .andExpect(status().isOk());
    }

    @Test
    void putBoard() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/boards/1")
                        .param("title", "1번 게시글 수정 ㅋ.")
                        .param("content", "1번 내용 수정 ㅋ")
                        .header(headerNm,this.token)
                        .cookie(new Cookie("CSRF_TOKEN", csrfToken())))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBoard() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/boards/1")
                        .header(headerNm,this.token)
                        .cookie(new Cookie("CSRF_TOKEN", csrfToken())))
                .andExpect(status().isOk());
    }

    @Test
    void getCommentList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/boards/1/comments"))
                .andExpect(status().isOk());
    }

    @Test
    void postCommentList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/boards/1/comments")
                        .param("title", "댓글 제목 등록 ㅋ.")
                        .param("content", "댓글 내용 등록 ㅋ.")
                        .header(headerNm,this.token)
                        .cookie(new Cookie("CSRF_TOKEN", csrfToken())))
                .andExpect(status().isOk());
    }

    @Test
    void putCommentList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/boards/1/comments/1")
                        .param("title", "댓글 제목 수정ㅋ ㅋ.")
                        .param("content", "댓글 내용 수정 ㅋ.")
                        .header(headerNm,this.token)
                        .cookie(new Cookie("CSRF_TOKEN", csrfToken())))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCommentList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/boards/1/comments/1")
                        .header(headerNm,this.token)
                        .cookie(new Cookie("CSRF_TOKEN", csrfToken())))
                .andExpect(status().isOk());
    }


    public String csrfToken(){
        String CSRF_TOKEN = "";
        if (!StringUtils.isEmpty(this.token) && this.token.startsWith("Bearer ")){
            CSRF_TOKEN  = this.token.substring(7).trim();
        }
        return CSRF_TOKEN;
    }


}
