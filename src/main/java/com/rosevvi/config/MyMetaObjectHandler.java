package com.rosevvi.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author: rosevvi
 * @date: 2023/3/25 21:54
 * @version: 1.0
 * @description:
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充=====insert");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getThreadLocal());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getThreadLocal());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充=====update");
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getThreadLocal());
    }
}
