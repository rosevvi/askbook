package com.rosevvi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosevvi.dao.AnswerDao;
import com.rosevvi.domain.Answer;
import com.rosevvi.service.AnswerService;
import org.springframework.stereotype.Service;

/**
 * @author: rosevvi
 * @date: 2023/4/5 14:15
 * @version: 1.0
 * @description:
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerDao, Answer> implements AnswerService {
}
