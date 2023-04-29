package com.leexm.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.Controller;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@Slf4j
@ControllerAdvice(annotations = {RestController.class})
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
        log.error("异常信息:{}",e.getMessage());

        if(e.getMessage().contains("Duplicate entry")){
            String[] split = e.getMessage().split(" ");
            return R.error(split[2]+"已存在");
        }

        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException e){
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }
}
