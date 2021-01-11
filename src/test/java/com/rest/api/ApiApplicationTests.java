package com.rest.api;

import com.rest.api.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiApplicationTests {

	@Autowired
	PostRepository postRepository;

	@Test
	void contextLoads() {
		System.out.println(postRepository.findAll());
	}
}
