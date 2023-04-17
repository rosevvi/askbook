package com.rosevvi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosevvi.config.BaseContext;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Type;
import com.rosevvi.dto.QuestionDto;
import com.rosevvi.service.QuestionService;
import com.rosevvi.service.TypeService;
import com.rosevvi.tools.Code;
import com.rosevvi.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * @author: rosevvi
 * @date: 2023/3/27 10:15
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TypeService typeService;

    /**
     * 获取问题列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<Question>> list(){
        LambdaQueryWrapper<Question> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Question::getIcon);
        List<Question> questions = questionService.list(lambdaQueryWrapper);
        return Result.success(Code.OK,"查询成功",questions);
    }

    /**
     * 添加问题
     * @param questionDto
     * @return
     */
    @PostMapping()
    public Result<Long> save(@RequestBody QuestionDto questionDto){
        Long id = questionService.saveQuestionAndType(questionDto);
        return Result.success(Code.OK,"添加成功",id);
    }

    /**
     * 查找单个问题
     * @param questionId
     * @return
     */
    @GetMapping("/{questionId}")
    public Result<QuestionDto> getOneQuestion(@PathVariable("questionId") Long questionId){
        //查询单个问题
        QuestionDto dto = questionService.getOneQuestion(questionId);
        return Result.success(Code.OK,"查询成功",dto);
    }

    /**
     * 查询问题分类
     * @return
     */
    @GetMapping("/type")
    public Result<List<Type>> getType(){
        List<Type> list = typeService.list();
        log.info("查询分类信息"+list.toString());
        return Result.success(Code.OK,"查询成功",list);
    }

    /**
     * 分页查询
     * @return
     */
    @GetMapping("/pageRandom")
    public Result<List<Question>> pageRandom(){
        return Result.success(Code.OK,"",questionService.pageForAnswerNum());
    }


    /**
     * 获取当前用户的问题列表
     * @return
     */
    @GetMapping
    public Result<List<Question>> getQuestionByNowUser(){
        LambdaQueryWrapper<Question> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getCreateUser, BaseContext.getThreadLocal());
        List<Question> list = questionService.list(queryWrapper);
        if (list.size()<1){
            return Result.error(Code.OK,"问题为空");
        }
        return Result.success(Code.OK,"查询成功",list);
    }

    /**
     * 根据类型查找问题
     * @param id
     * @return
     */
    @GetMapping("/byType/{id}")
    public Result<List<Question>> getQuestionByType(@PathVariable("id") Long id){
        List<Question> questions = questionService.getQuestionByType(id);
        return Result.success(Code.OK,"查找成功",questions);
    }

    /**
     * 根据问题id删除问题
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteQuestionById(@PathVariable("id") Long id){
        questionService.deleteByQuestionId(id);
        return Result.success(Code.OK,"删除成功",null);
    }

    /**
     * 更新问题内容
     * @param questionDto
     * @return
     */
    @PutMapping()
    public Result<String> updateQuestion(@RequestBody QuestionDto questionDto){
        questionService.updateQuestion(questionDto);
        return Result.success(Code.OK,"更新成功",null);
    }

}
