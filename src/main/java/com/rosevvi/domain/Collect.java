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
 * @date: 2023/4/6 10:13
 * @version: 1.0
 * @description:
 */

@Data
public class Collect implements Serializable {

    public static final Long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private Long textId;

    private Long questionId;

    private Long toUserId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private Long createUser;

    @TableField(exist = false)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private Long updateUser;
}
