package com.rosevvi.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: rosevvi
 * @date: 2023/4/16 16:11
 * @version: 1.0
 * @description:
 */
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long toUserId;

    private String message;

    private String flag;

    private Long questionId;

    private Long textId;

    private Long questionUserSize;





}
