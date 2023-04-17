package com.rosevvi.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/3/27 9:47
 * @version: 1.0
 * @description:
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question implements Serializable {
    public static final Long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    private String data;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Long icon;

    private Integer solved;

    private String images;

    //回答数量
    @TableField(exist = false)
    private Long answerNum;
}
