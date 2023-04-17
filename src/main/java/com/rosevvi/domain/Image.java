package com.rosevvi.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: rosevvi
 * @date: 2023/4/15 13:54
 * @version: 1.0
 * @description:
 */
@Data
public class Image implements Serializable {
    public static final Long serialVersionUID = 1L;

    //图片src
    private String url;
    //图片描述文字
    private String alt;
    //图片链接
    private String href;

}
