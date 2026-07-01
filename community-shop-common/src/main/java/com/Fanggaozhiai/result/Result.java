package com.Fanggaozhiai.result;

import lombok.Data;

/**
 * 统一响应结果类
 * 所有API接口返回此对象，前端通过code判断请求是否成功
 * code: 200成功 500失败
 */
@Data
public class Result {

    /** 状态码，200成功 500失败 */
    private Integer code;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private Object data;

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     * @return 成功结果
     */
    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（无数据）
     *
     * @return 成功结果
     */
    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(null);
        return result;
    }

    /**
     * 失败响应
     *
     * @param message 错误消息
     * @return 失败结果
     */
    public static Result error(String message) {
        Result result = new Result();
        result.setCode(500);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}