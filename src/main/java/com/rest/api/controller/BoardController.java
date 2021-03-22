package com.rest.api.controller;

import com.rest.api.domain.Board;
import com.rest.api.domain.Comment;
import com.rest.api.domain.User;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.repository.BoardQueryRepository;
import com.rest.api.util.CookieUtils;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static com.rest.api.domain.QBoard.board;

/**
 *
 * Description : 게시판 컨트롤러
 *
 * Modification Information
 * 수정일			 수정자						수정내용
 * -----------	-----------------------------  -------
 * 2021. 3.  22.    MICHAEL						최초작성
 *
 */
@RequiredArgsConstructor
@RestController
public class BoardController {

    private Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardQueryRepository boardQueryRepository;

    /**
     * 게시판 리스트 조회
     * @param pageRequest
     * @param ordr
     * @param ordrNm
     * @param title
     * @param content
     * @param req
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 게시판 리스트 조회
    @GetMapping("/board/all")
    public ResponseMessage listAll(
             @PageableDefault(page = 0, size = 10) PageRequest pageRequest,
             @RequestParam(value="ordr" , defaultValue = "DESC") String ordr,
             @RequestParam(value="ordrNm" , defaultValue = "id") String ordrNm,
             @RequestParam(value="title" , defaultValue = "") String title,
             @RequestParam(value="content" , defaultValue = "") String content , HttpServletRequest req) throws Exception {
        ResponseMessage ms = new ResponseMessage();
        if (!StringUtils.isEmpty(title) || !StringUtils.isEmpty(content)) {
            pageRequest.setPage(1);
        }
        Page<Board> board = boardQueryRepository.getList(pageRequest.of(ordr,ordrNm) , title , content);
        ms.add("result",board);
        return ms;
    }

    /**
     * 게시글 조회
     * @param id
     * @param req
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 게시글 조회
    @GetMapping("/board/{id}")
    public ResponseMessage getBoard( @PathVariable Long id , HttpServletRequest req) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        ms.add("result",boardQueryRepository.selectOne(id));
        return ms;
    }

    /**
     * 게시글 등록
     * @param board
     * @param req
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 게시글 등록
    @PostMapping("/boards")
    public ResponseMessage save(Board board , HttpServletRequest req) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        boardQueryRepository.save(board);
        return ms;
    }

    /**
     * 게시글 수정
     * @param id
     * @param board
     * @param req
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 게시글 수정
    @PutMapping("/boards/{id}")
    public ResponseMessage update(@PathVariable Long id ,Board board , HttpServletRequest req) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        board.setId(id);
        boardQueryRepository.update(board);
        return ms;
    }

    /**
     * 게시글 삭제
     * @param id
     * @param board
     * @param req
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 게시글 삭제
    @DeleteMapping("/boards/{id}")
    public ResponseMessage delete(@PathVariable Long id ,Board board , HttpServletRequest req) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        board.setId(id);

        if(boardQueryRepository.commentCount(board) == 0){
            boardQueryRepository.delete(board);
        }else{
            ms.setMessage("댓글이 존재하는 게시글은 삭제할 수 없습니다.");
        }
        return ms;
    }

    /**
     * 게시글 댓글 전체 조회
     * @param id
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 게시글 댓글 전체 조회
    //@CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/board/{id}/comment")
    public ResponseMessage  getPostComments(@PathVariable Long id){
        ResponseMessage ms = new ResponseMessage();
        Board board = boardQueryRepository.findById(id);
        ms.add("result",boardQueryRepository.findCommentsByComment(board));
        return ms;
    }

    /**
     * 게시글 댓글 등록
     * @param id
     * @param comment
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 게시글 댓글 등록
    //@CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/board/{id}/comment")
    public ResponseMessage createComment(@PathVariable Long id, Comment comment){
        ResponseMessage ms = new ResponseMessage();
        Board board = boardQueryRepository.findById(id);
        comment.setBoard(board);
        boardQueryRepository.commentSave(comment);
        return ms;
    }

    /**
     * 게시글 댓글 수정
     * @param id
     * @param commentId
     * @param comment
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 게시글 댓글 수정
    //@CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/board/{id}/comment/{commentId}")
    public ResponseMessage updateComment(@PathVariable Long id, @PathVariable Long commentId, Comment comment){
        ResponseMessage ms = new ResponseMessage();
        Board board = boardQueryRepository.findById(id);
        comment.setComment_id(commentId);
        comment.setBoard(board);
        boardQueryRepository.commentUpdate(comment);
        return ms;
    }


    /**
     * 게시글 댓글 삭제
     * @param id
     * @param commentId
     * @param comment
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 게시글 댓글 삭제
    //@CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/board/{id}/comment/{commentId}")
    public ResponseMessage deleteComment(@PathVariable Long id, @PathVariable Long commentId , Comment comment){
        ResponseMessage ms = new ResponseMessage();
        comment.setComment_id(commentId);
        boardQueryRepository.commentDelete(comment);
        return ms;
    }

}
