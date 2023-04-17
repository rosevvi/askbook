package com.rosevvi.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: rosevvi
 * @date: 2023/4/8 17:05
 * @version: 1.0
 * @description:
 */
@Data
public class Comment implements Serializable {
    public static final Long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String data;

    private Long userId;

    private Long answerId;

    private Long textId;

    private Long commentId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private Long createUser;

    @TableField(exist = false)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private Long updateUser;

    private Long toUserId;

    private Long questionId;
}
