package com.ahdark.code.link.utils;

/**
 * 规定:
 * #1表示成功
 * #1001～1999 区间表示参数错误
 * #2001～2999 区间表示用户错误
 * #3001～3999 区间表示接口异常
 *
 * @Author AH-dark
 * @Description 返回码定义
 */
public enum CodeInfo {
    /* 成功 */
    SUCCESS(200, "success"),

    /* 默认失败 */
    COMMON_FAIL(400, "failed"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "Parameter is invalid"),
    PARAM_IS_BLANK(1002, "Parameter is empty"),
    PARAM_TYPE_ERROR(1003, "Parameter type error"),
    PARAM_NOT_COMPLETE(1004, "Parameter missing"),

    /* 用户错误 */
    USER_NOT_LOGIN(2001, "User is not logged in"),
    USER_ACCOUNT_EXPIRED(2002, "Account has expired"),
    USER_CREDENTIALS_ERROR(2003, "Password error"),
    USER_CREDENTIALS_EXPIRED(2004, "Password expired"),
    USER_ACCOUNT_DISABLE(2005, "Account unavailable"),
    USER_ACCOUNT_LOCKED(2006, "Account is locked"),
    USER_ACCOUNT_NOT_EXIST(2007, "Account does not exist"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "The account already exists"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "Account offline"),

    /* 数据错误 */
    DATA_IS_EXIST(3001, "Data is exise"),

    /* 业务错误 */
    NO_PERMISSION(403, "Permission denied"),
    NO_DATA(404, "Not found");

    private final Integer code;
    private final String msg;

    CodeInfo(Integer code, String success) {
        this.code = code;
        this.msg = success;
    }

    public static String getMessageByCode(Integer code) {
        for (CodeInfo ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMsg();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
