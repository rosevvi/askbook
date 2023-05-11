package com.rosevvi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import com.rosevvi.dto.SearchDto;
import com.rosevvi.service.QuestionService;
import com.rosevvi.service.TextService;
import com.rosevvi.tools.Code;
import com.rosevvi.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/19 17:11
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TextService textService;


    @GetMapping()
    public Result<SearchDto> search(String title){
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper=new LambdaQueryWrapper<>();
        questionLambdaQueryWrapper.like(Question::getTitle,title);
        List<Question> questions = questionService.list(questionLambdaQueryWrapper);

        LambdaQueryWrapper<Text> textLambdaQueryWrapper=new LambdaQueryWrapper<>();
        textLambdaQueryWrapper.like(Text::getTitle,title);
        List<Text> texts = textService.list(textLambdaQueryWrapper);

        SearchDto searchDto=new SearchDto();
        searchDto.setQuestions(questions);
        searchDto.setTexts(texts);

        return Result.success(Code.OK,"查询成功",searchDto);
    }

}
