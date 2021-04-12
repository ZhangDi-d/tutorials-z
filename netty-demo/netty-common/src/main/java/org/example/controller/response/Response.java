package org.example.controller.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.example.enums.ErrCode;


/**
 * @author jiangyun
 * @version 0.0.1
 * @date 2021/3/3 18:09
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record Response<T>(String code, String msg, T data) {

    public static <T> Response<T> ok(T data) {
        return new Response<>(ErrCode.SUCCESS.getCode(), ErrCode.SUCCESS.getMessage(), data);
    }

    public static <T> Response<T> error(String code, String msg) {
        return new Response<>(code, msg, null);
    }

    public static <T> Response<T> error(ErrCode errCode) {
        return new Response<>(errCode.getCode(), errCode.getMessage(), null);
    }

}
