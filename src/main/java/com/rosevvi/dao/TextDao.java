package com.rosevvi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosevvi.domain.Text;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: rosevvi
 * @date: 2023/4/1 13:07
 * @version: 1.0
 * @description:
 */
@Mapper
public interface TextDao extends BaseMapper<Text> {
}
