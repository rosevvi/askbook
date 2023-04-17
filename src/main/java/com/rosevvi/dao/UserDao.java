package com.rosevvi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosevvi.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: rosevvi
 * @date: 2023/3/22 14:59
 * @version: 1.0
 * @description:
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
}
