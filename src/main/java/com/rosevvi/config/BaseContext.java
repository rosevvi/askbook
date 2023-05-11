package com.rosevvi.config;

import org.springframework.stereotype.Component;

/**
 * @author: rosevvi
 * @date: 2023/3/26 10:32
 * @version: 1.0
 * @description:
 */
@Component
public class BaseContext {
    public static final ThreadLocal<Long> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void setThreadLocal(Long id){
        THREAD_LOCAL.set(id);
    }

    public static Long getThreadLocal(){
        return THREAD_LOCAL.get();
    }
}
