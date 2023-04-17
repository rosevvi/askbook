package com.rosevvi.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: rosevvi
 * @date: 2023/4/1 12:59
 * @version: 1.0
 * @description:
 */
@Data
public class Type implements Serializable {
    private static final Long serialVersionUID=1L;

    private Long id;

    private String type;

}
