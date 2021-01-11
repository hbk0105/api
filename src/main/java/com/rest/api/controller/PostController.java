package com.rest.api.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rest.api.domain.Post;
import com.rest.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api/posts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    @Autowired
    PostRepository postRepository;

    // Post 리스트 조회
    @GetMapping(value = "") // TODO FIX TYPE
    public ResponseEntity getPostList( @RequestParam(value = "order", required = false) String order) {
        return new ResponseEntity<>(postRepository.findAll(), HttpStatus.OK);
    }

    // {postId}번 Post 조회
    @GetMapping("/{postId}")
    public ResponseEntity getPost(@PathVariable Long postId) {
        return new ResponseEntity<>(postRepository.getOne(postId), HttpStatus.OK);
    }

}
