package com.rest.api.exception;

import com.rest.api.util.ResponseMessage;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // https://lottogame.tistory.com/3916
    // https://eblo.tistory.com/48

    // 자주 발생하는 예외
    // https://velog.io/@gillog/Java-%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC-Exception

    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(value=HttpStatus.UNAUTHORIZED)
    public ResponseMessage handleAccessDeniedException(HttpServletRequest req , final AccessDeniedException exception) {
        return new ResponseMessage(HttpStatus.UNAUTHORIZED, exception.getMessage(), req.getRequestURL().toString());
    }

    // 500
    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final RuntimeException exception)  {
        return new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final Exception exception)  {
        return new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final NotFoundException exception)  {
        return new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final NullPointerException exception)  {
        return new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final NoResultException exception)  {
        return new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final IllegalArgumentException exception)  {
        return new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), req.getRequestURL().toString());
    }

}
