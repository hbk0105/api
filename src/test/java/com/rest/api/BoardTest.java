package com.rest.api;

import ch.qos.logback.core.CoreConstants;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rest.api.domain.Board;
import com.rest.api.domain.BoardPaginationDto;
import com.rest.api.repository.BoardQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.List;

import static com.rest.api.domain.QBoard.board;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@Transactional
@Commit
public class BoardTest {

    @Autowired
    private BoardQueryRepository boardQueryRepository;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {

        for(int i = 0; i < 100; i++){
            boardQueryRepository.save(Board.builder().name("name " + i).content("content- " +i).build());
        }

    }

    @Test //  Spring Data Jpa Custom Repository 적용
    public void 테스트() {

        //when
        List<Board> result = boardQueryRepository.findByAll();

        System.out.println("result :: " + result);


    }

    @Test
    void 기존_페이징_방식() throws Exception {
        //given
        String name = "1";

        /*
        page : 검색을 원하는 페이지 번호입니다.
        size : 한 페이지의 조회할 게시물 개수를 나타냅니다.
        */

        //when
        Pageable pageable = PageRequest.of(1, 10, Sort.Direction.ASC, "name");

        List<BoardPaginationDto> books = boardQueryRepository.paginationCoveringIndex(name, 1, 10 , pageable);

        System.out.println("books : " + books.size());


        for(int i = 0; i < books.size(); i ++){
            System.out.println(" name :: " + books.get(i).getName());
        }


        //then
         //assertThat(board, is(10));
        //assertThat(board).hasSize(10);
    }

}
