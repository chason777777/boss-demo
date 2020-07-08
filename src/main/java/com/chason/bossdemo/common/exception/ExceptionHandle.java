package com.chason.bossdemo.common.exception;

import com.chason.bossdemo.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by  Jason on 2017/12/11.
 */
@ControllerAdvice
public class ExceptionHandle {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandle.class);

    /**
     * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionGet(Exception e) {
        LOGGER.error("【系统异常】{}", e);
        return Result.error(e.getMessage());
    }
}
