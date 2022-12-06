package com.example.school.service.base.handler;

import com.example.school.common.base.result.R;
import com.example.school.common.base.result.ResultCodeEnum;
import com.example.school.common.base.util.ExceptionUtils;
import com.example.school.service.base.exception.SchoolException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 加上异常处理器注解，可以处理Exception类型的异常
    // 即使是遇到了Exception，也会返回R的结果
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e) {
        //e.printStackTrace();
        log.error(ExceptionUtils.getMessage(e));
        return R.error();
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public R error(BadSqlGrammarException e) {
        //e.printStackTrace();
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e) {
        //e.printStackTrace();
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(SchoolException.class)
    @ResponseBody
    public R error(SchoolException e) {
        //e.printStackTrace();
        log.error(ExceptionUtils.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
