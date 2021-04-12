package org.example.enums;

public enum ErrCode {

    /**
     * 接口响应状态码
     */
    SUCCESS("000000", "success"),
    SYSTEM_ERR("999999", "system exception"),

    PARAM_ERROR("100001", "param error"),
    MODEL_PREHEATING_UNFINISHED("100002", "please waiting..."),
    ;

    private final String code;

    private final String message;

    ErrCode(String code, String message) {
        this.code    = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
