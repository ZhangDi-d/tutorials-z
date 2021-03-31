package org.example.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @author dizhang
 * @date 2021-03-18
 */
public class Response<T> implements Serializable {

    public static Integer CODE_SUCCESS = 0;

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    /**
     * 将传入的 result 对象，转换成另外一个泛型结果的对象
     *
     * 因为 A 方法返回的 CommonResult 对象，不满足调用其的 B 方法的返回，所以需要进行转换。
     *
     * @param result 传入的 result 对象
     * @param <T> 返回的泛型
     * @return 新的 CommonResult 对象
     */
    public static <T> Response<T> error(Response<?> result) {
        return error(result.getCode(), result.getMessage());
    }

    public static <T> Response<T> error(Integer code, String message) {
        Assert.isTrue(!CODE_SUCCESS.equals(code), "code must not be success code:0 !");
        Response<T> result = new Response<>();
        result.code = code;
        result.message = message;
        return result;
    }

    public static <T> Response<T> success(T data) {
        Response<T> result = new Response<>();
        result.code = CODE_SUCCESS;
        result.data = data;
        result.message = "";
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return CODE_SUCCESS.equals(code);
    }

    @JsonIgnore
    public boolean isError() {
        return !isSuccess();
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
