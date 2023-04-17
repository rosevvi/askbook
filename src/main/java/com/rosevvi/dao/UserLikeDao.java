package com.rosevvi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosevvi.domain.UserLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: rosevvi
 * @date: 2023/4/6 11:26
 * @version: 1.0
 * @description:
 */
@Mapper
public interface UserLikeDao extends BaseMapper<UserLike> {
}
