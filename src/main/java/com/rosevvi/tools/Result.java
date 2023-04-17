package com.rosevvi.tools;

import lombok.Data;

/**
 * @author: rosevvi
 * @date: 2023/3/22 14:27
 * @version: 1.0
 * @description:
 */
@Data
public class Result<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private T data; //数据

    private String msg;

    public static <T> Result<T> success(Integer code,String msg,T data){
        Result<T> result=new Result<>();
        result.code=code;
        result.msg=msg;
        result.data=data;
        return result;
    }
    public static <T> Result<T> success(Integer code,String msg){
        Result<T> result=new Result<>();
        result.code=code;
        result.msg=msg;
        return result;
    }


    public static <T> Result<T> error(Integer code,String msg){
        Result<T> result=new Result<>();
        result.code=code;
        result.msg=msg;
        return  result;
    }

}
