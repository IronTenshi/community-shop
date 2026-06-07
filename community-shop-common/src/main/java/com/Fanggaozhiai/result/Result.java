package com.Fanggaozhiai.result;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String message;
    private Object data;

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }

    public static Result success(){
        Result result = new Result();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(null);
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(500);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

}
