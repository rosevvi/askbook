package com.rosevvi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosevvi.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: rosevvi
 * @date: 2023/4/8 17:07
 * @version: 1.0
 * @description:
 */
@Mapper
public interface CommentDao extends BaseMapper<Comment> {
}
