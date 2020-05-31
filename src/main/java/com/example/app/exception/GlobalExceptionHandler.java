package com.example.app.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常处理
 *
 * @author <a href="mailto:guzhongtao@middol.com">guzhongtao</a>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 程序运行异常
     *
     * @param e Exception
     * @return ResponseVO
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleException(Throwable e) {
        e.printStackTrace();
        return e.getMessage();
    }

    /**
     * 自定义业务异常
     *
     * @param e BusinessException
     * @return ResponseVO
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public String handleBusinessException(BusinessException e) {
        return e.getErrorMsg();
    }

    /**
     * 用户访问接口的权限验证不通过
     *
     * @param e UnauthorizedException
     * @return ResponseVO
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public String handleUnauthorizedException(UnauthorizedException e) {
        return e.getLocalizedMessage();
    }

}