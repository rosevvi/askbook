package com.rosevvi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosevvi.dao.UserDao;
import com.rosevvi.domain.User;
import com.rosevvi.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author: rosevvi
 * @date: 2023/3/22 15:01
 * @version: 1.0
 * @description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
}
