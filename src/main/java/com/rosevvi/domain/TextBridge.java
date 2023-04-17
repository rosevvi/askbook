package com.rosevvi.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: rosevvi
 * @date: 2023/4/3 16:24
 * @version: 1.0
 * @description:
 */
@Data
public class TextBridge implements Serializable {
    private static final Long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long textId;

    private Long typeId;

    public TextBridge(Long textId,Long typeId){
        this.textId=textId;
        this.typeId=typeId;
    }
}
