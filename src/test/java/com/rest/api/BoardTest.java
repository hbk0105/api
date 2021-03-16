package com.rest.api;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rest.api.domain.Board;
import com.rest.api.repository.BoardQueryRepository;
import com.rest.api.util.PageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
@Commit
public class BoardTest {

    @Autowired
    private BoardQueryRepository boardQueryRepository;

    JPAQueryFactory queryFactory;

    //@BeforeEach
    public void before() {

        for(int i = 0; i < 100; i++){
            boardQueryRepository.save(Board.builder().title("name " + i).content("content- " +i).build());
        }

        // 수정
        boardQueryRepository.update(Board.builder().id((long) 1).title("수정이지롱").content("내용은..").build());

        // 삭제
        boardQueryRepository.delete(Board.builder().id((long) 2).build());



    }



    @Test
    void 기존_페이징_방식() throws Exception {
        //given
        String name = "";

        /*
        page : 검색을 원하는 페이지 번호입니다.
        size : 한 페이지의 조회할 게시물 개수를 나타냅니다.
        */

        PageRequest pageRequest = new PageRequest();
        //pageRequest.setPage(0);
        //pageRequest.setSize(10);
        //pageRequest.setDirection(Sort.Direction.ASC);

        //when
        //Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 2, Sort.Direction.ASC, "id");

       /* List<BoardPaginationDto> books = boardQueryRepository.paginationCoveringIndex(name , pageRequest.of());

        for(int i = 0; i < books.size(); i ++){
            System.out.println(" name :: " + books.get(i).getName());
        }

        System.out.println("-----------------------------------------");
*/
        //then
        //assertThat(board, is(10));
        //assertThat(board).hasSize(10);

        System.out.println("-----------------------------------------");
        System.out.println("-----------------------------------------");
        Page<Board> list =  boardQueryRepository.getList(pageRequest.of(),"","1");
        System.out.println(list.getContent().get(0).getTitle());
        System.out.println("list.getTotalElements() :: " + list.getTotalElements());

    }

}
