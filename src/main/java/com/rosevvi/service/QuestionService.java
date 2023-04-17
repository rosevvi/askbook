package com.rosevvi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Type;
import com.rosevvi.dto.QuestionDto;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/3/27 10:12
 * @version: 1.0
 * @description:
 */
public interface QuestionService extends IService<Question> {

    public Long saveQuestionAndType(QuestionDto questionDto);

    public List<Question> pageForAnswerNum();

    public QuestionDto getOneQuestion(Long questionId);

    public List<Question> getQuestionByType(Long id);

    public void deleteByQuestionId(Long id);

    public void updateQuestion(QuestionDto questionDto);
}
