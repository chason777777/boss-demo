package com.chason.bossdemo.common.constant;

public enum ErrorType {
    PARAM_ISNULL("100", "参数不能为空"),
    RESULT_PARAMS_FAIL("401", "数据参数不对"),   //必需的参数没有传或参数类型不对null值
//    SN_IS_WRONG("101", "SN格式不正确"),
    SN_IS_WRONG("102", "SN格式不正确"),
    PARAM_DATA_WRONG("103", "参数数据不正确"),
    OTAUP_FILE_ISNULL("112", "查无升级文件"),
    OTAUP_TASK_ISNULL("113", "升级任务不存在"),
    RESULT_ISNULL("210", "查无结果"),
    RESULT_ISFAIL("211", "查询失败"),
    RESULT_FAIL("212", "操作失败"),
    FILE_ALREADY_EXISTS("213", "升级文件已经存在"),
    USERNAME_PASSWORD_IS_WRONG("442", "用户或密码错误"),
    LOGIN_FAIL("443", "登陆失败"),
    LOGIN_NO("444", "没有登录"),//没有登录
    HAVE_NO_PERMISSION("445", "没有权限"),
    USER_EXISTS("450", "用户已存在"),
    USER_UNEXISTS("451", "用户不存在");

    private String key;

    private String value;

    ErrorType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
