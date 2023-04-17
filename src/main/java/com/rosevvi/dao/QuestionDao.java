package com.rosevvi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosevvi.domain.Question;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: rosevvi
 * @date: 2023/3/27 10:11
 * @version: 1.0
 * @description:
 */
@Mapper
public interface QuestionDao extends BaseMapper<Question> {
}
