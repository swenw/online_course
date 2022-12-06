package com.example.school.service.base.exception;

// 普通的Exception是检查类异常，要么捕获要么继续向上抛出，会形成死循环

import com.example.school.common.base.result.ResultCodeEnum;
import lombok.Data;

@Data
public class SchoolException extends RuntimeException {
    // 错误码
    private Integer code;

    public SchoolException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public SchoolException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "SchoolException{" +
                "code=" + code +
                ", message" + this.getMessage() +
                "}";
    }
}
