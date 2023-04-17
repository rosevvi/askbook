package com.rosevvi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosevvi.domain.Answer;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: rosevvi
 * @date: 2023/4/5 14:14
 * @version: 1.0
 * @description:
 */
@Mapper
public interface AnswerDao extends BaseMapper<Answer> {
}
