package com.rest.api.controller;

import com.rest.api.domain.Board;
import com.rest.api.domain.Comment;
import com.rest.api.domain.User;
import com.rest.api.jwt.JwtTokenUtil;
import com.rest.api.repository.BoardQueryRepository;
import com.rest.api.service.UserService;
import com.rest.api.util.CookieUtils;
import com.rest.api.util.PageRequest;
import com.rest.api.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

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
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BoardQueryRepository boardQueryRepository;

    @Autowired
    private UserService userService;

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
    @GetMapping("/boards")
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
        try {
            Page<Board.Response> board = boardQueryRepository.getList(pageRequest.of(ordr,ordrNm) , title , content);
            ms.add("result",board);

        }catch (Exception e){
            e.printStackTrace();
        }

        //  Page<Board.Response> result = new PageImpl<>(board.getTotalElements(), pageRequest.of(ordr,ordrNm),board.get().count());
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
    @GetMapping("/boards/{id}")
    public ResponseMessage getBoard( @PathVariable Long id , HttpServletRequest req) throws Exception{
        ResponseMessage ms = new ResponseMessage();

        Board borad = boardQueryRepository.selectOne(id);

        ms.add("result",Board.Response.builder()
                .id(borad.getId())
                .title(borad.getTitle())
                .content(borad.getContent())
                .email(borad.getUser().getEmail())
                .firstName(borad.getUser().getFirstName())
                .lastName(borad.getUser().getLastName()).build());
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
    public ResponseMessage save(Board board , HttpServletRequest req,HttpServletResponse res) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        User user =  Optional.ofNullable(getUser(req,res)).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다."));
        //if(user == null) return ms = new ResponseMessage(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.", req.getRequestURL().toString());
        board.setUser(user);
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
    public ResponseMessage update(@PathVariable Long id ,Board board , HttpServletRequest req,HttpServletResponse res) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        User user =  Optional.ofNullable(getUser(req,res)).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다."));
        //if(user == null) return ms = new ResponseMessage(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.", req.getRequestURL().toString());
        board.setUser(user);
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
    public ResponseMessage delete(@PathVariable Long id ,Board board , HttpServletRequest req,HttpServletResponse res) throws Exception{
        ResponseMessage ms = new ResponseMessage();
        board.setId(id);
        User user =  Optional.ofNullable(getUser(req,res)).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다."));
        //if(user == null) return ms = new ResponseMessage(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.", req.getRequestURL().toString());
        board.setUser(user);
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
    @GetMapping("/boards/{id}/comments")
    public ResponseMessage  getPostComments(@PathVariable Long id , @PageableDefault(page = 0, size = 10) PageRequest pageRequest) throws Exception {
        ResponseMessage ms = new ResponseMessage();
        Board board = boardQueryRepository.findById(id);
        ms.add("result",boardQueryRepository.findByComment(board , pageRequest.of()));
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
    @PostMapping("/boards/{id}/comments")
    public ResponseMessage createComment(@PathVariable Long id, Comment comment , HttpServletRequest req ,  HttpServletResponse res){
        ResponseMessage ms = new ResponseMessage();
        // https://cselabnotes.com/kr/2021/03/31/60/
        Board board = Optional.ofNullable(boardQueryRepository.findById(id)).orElseThrow(() -> new NoResultException("게시글이 존재하지 않습니다."));
        User user =  Optional.ofNullable(getUser(req,res)).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다."));
        //if(user == null) return ms = new ResponseMessage(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.", req.getRequestURL().toString());
        board.setUser(user);
        comment.setUser(user);
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
    @PutMapping("/boards/{id}/comments/{commentId}")
    public ResponseMessage updateComment(@PathVariable Long id, @PathVariable Long commentId
            , Comment comment , HttpServletRequest req , HttpServletResponse res){
        ResponseMessage ms = new ResponseMessage();
        Board board = boardQueryRepository.findById(id);
        User user =  Optional.ofNullable(getUser(req,res)).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다."));
        //if(user == null) return ms = new ResponseMessage(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.", req.getRequestURL().toString());
        board.setUser(user);
        comment.setUser(user);
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
    // @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/boards/{id}/comments/{commentId}")
    public ResponseMessage deleteComment(@PathVariable Long id, @PathVariable Long commentId
            , Comment comment , HttpServletRequest req ,  HttpServletResponse res){
        ResponseMessage ms = new ResponseMessage();
        Board board = boardQueryRepository.findById(id);
        User user =  Optional.ofNullable(getUser(req,res)).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다."));
        //if(user == null) return ms = new ResponseMessage(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.", req.getRequestURL().toString());
        board.setUser(user);
        comment.setUser(user);
        comment.setComment_id(commentId);
        boardQueryRepository.commentDelete(comment);
        return ms;
    }

    private User getUser(HttpServletRequest req , HttpServletResponse res){
        User user = null;
        try {
            String token = CookieUtils.accessToken(req, res,jwtTokenUtil);
            if(StringUtils.isEmpty(token)){
                token = CookieUtils.refreshToken(req , res,jwtTokenUtil);
            }
            String username = jwtTokenUtil.getUsername(token);
            if(!StringUtils.isEmpty(username)) {
                String r[] = username.split("-");
                Long id = Long.parseLong(r[0]);
                Optional<User> users  = Optional.ofNullable(userService.findById(id).orElseThrow(() -> new NoResultException("사용자가 존재하지 않습니다.")));
                user = users.get();
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            return user;
        }
    }

}
