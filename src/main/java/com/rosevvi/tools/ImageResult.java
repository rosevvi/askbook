package com.rosevvi.tools;

import lombok.Data;

/**
 * @author: rosevvi
 * @date: 2023/4/15 13:50
 * @version: 1.0
 * @description:
 */
@Data
public class ImageResult<T> {
    private Integer errno;

    private T data;

    private String message;

    public static <T> ImageResult<T> success(Integer errno,String message,T data){
        ImageResult<T> imageResult=new ImageResult<>();
        imageResult.errno=errno;
        imageResult.data=data;
        imageResult.message=message;
        return imageResult;
    }

    public static <T> ImageResult<T> success(Integer errno,String message){
        ImageResult<T> imageResult=new ImageResult<>();
        imageResult.errno=errno;
        imageResult.message=message;
        return imageResult;
    }
}
