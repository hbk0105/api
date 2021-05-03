package com.rest.api;

import com.rest.api.elasticsearch.Blog;
import com.rest.api.elasticsearch.BlogEsRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // 시큐리티 같은 설정 타지않음 ,http://blog.devenjoy.com/?p=524
@Transactional
public class ElasticsearchTest {

    private Logger log = LoggerFactory.getLogger(ElasticsearchTest.class);

    @Autowired
    BlogEsRepository blogEsRepository;

    @Test
    void test() {
        Blog blog = new Blog();
        blog.setId("1");
        blog.setContent("내용입니다.");
        blog.setTitle("제목입니다.");
        blogEsRepository.save(blog);
    }


}
