package com.chason.bossdemo.common;

/**
 * Created by caolh on 2018/2/2.
 */
public class Result<T> {

    private String code;//状态-1非正常，0正常
    private String msg;//null 正常；字符串 错误信息
    private T data;//数据类型

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 正确返回：status=1，msg=null，data 数据
     *
     * @param o
     * @return
     */
    public static Result success(Object o) {
        Result result = new Result();
        String code = "200";

        result.setCode(code);
        result.setMsg(null);
        result.setData(o);
        return result;
    }

    /**
     * 正确返回：status=1，msg，data=null 数据
     *
     * @param str
     * @return
     */
    public static Result success(String str) {
        Result result = new Result();
        String code = "200";
        result.setCode(code);
        result.setMsg(str);
        result.setData(null);
        return result;
    }

    /**
     * 正确返回：status=1，msg=提示信息，data 数据
     *
     * @param o
     * @param msg
     * @return
     */
    public static Result success(Object o, String msg) {
        Result result = new Result();
        String code = "200";
        result.setCode(code);
        result.setMsg(msg);
        result.setData(o);
        return result;
    }

    /**
     * 异常返回：status=0，msg= 异常信息，data 数据为null
     *
     * @param error
     * @return
     */
    public static Result error(String error) {
        Result result = new Result();
        String code = "500";
        result.setCode(code);
        result.setMsg(error);
        result.setData(null);
        return result;
    }


    /**
     * 异常返回：status=0，msg= 异常信息，data 数据为null
     *
     * @param error
     * @return
     */
    public static Result error(String code, String error) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(error);
        result.setData(null);
        return result;
    }

}
