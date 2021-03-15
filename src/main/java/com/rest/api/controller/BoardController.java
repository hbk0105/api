package com.rest.api.controller;

import com.rest.api.domain.Board;
import com.rest.api.domain.User;
import com.rest.api.repository.BoardQueryRepository;
import com.rest.api.util.PageRequest;
import com.rest.api.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static com.rest.api.domain.QBoard.board;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardQueryRepository boardQueryRepository;

    @GetMapping("/board/all")
    public ResponseMessage listAll(
             @PageableDefault(page = 0, size = 10) PageRequest pageRequest,
             @RequestParam(value="ordr" , defaultValue = "DESC") String ordr,
             @RequestParam(value="ordrNm" , defaultValue = "id") String ordrNm,
             @RequestParam(value="name" , defaultValue = "") String name,
             @RequestParam(value="content" , defaultValue = "") String content , HttpServletRequest req) throws Exception {
        ResponseMessage ms = new ResponseMessage();
        if (!StringUtils.isEmpty(name) || !StringUtils.isEmpty(content)) {
            pageRequest.setPage(1);
        }
        Page<Board> board = boardQueryRepository.getList(pageRequest.of(ordr,ordrNm) , name , content);
        ms.add("result",board);
        return ms;
    }

    @GetMapping("/board/{id}")
    public ResponseMessage getBoard( @PathVariable Long id , HttpServletRequest req) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        ms.add("result",boardQueryRepository.selectOne(id));
        return ms;
    }

    @PostMapping("/boards")
    public ResponseMessage save(Board board , HttpServletRequest req) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        boardQueryRepository.save(board);
        return ms;
    }

    @PutMapping("/boards/{id}")
    public ResponseMessage update(@PathVariable Long id ,Board board , HttpServletRequest req) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        board.setId(id);
        boardQueryRepository.update(board);
        return ms;
    }

    @DeleteMapping("/boards/{id}")
    public ResponseMessage delete(@PathVariable Long id ,Board board , HttpServletRequest req) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        board.setId(id);
        boardQueryRepository.delete(board);
        return ms;
    }

}
