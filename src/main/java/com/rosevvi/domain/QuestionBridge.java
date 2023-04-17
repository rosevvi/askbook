package com.rosevvi.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: rosevvi
 * @date: 2023/4/3 19:43
 * @version: 1.0
 * @description:
 */
@Data
public class QuestionBridge implements Serializable {
    public static final Long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long QuestionId;

    private Long TypeId;

    public QuestionBridge(Long QuestionId,Long TypeId){
        this.QuestionId=QuestionId;
        this.TypeId=TypeId;
    }

}
