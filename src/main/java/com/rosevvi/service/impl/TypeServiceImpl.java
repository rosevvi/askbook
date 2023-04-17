package com.rosevvi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosevvi.dao.TypeDao;
import com.rosevvi.domain.Type;
import com.rosevvi.service.TypeService;
import org.springframework.stereotype.Service;

/**
 * @author: rosevvi
 * @date: 2023/4/1 13:02
 * @version: 1.0
 * @description:
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeDao, Type> implements TypeService {
}
