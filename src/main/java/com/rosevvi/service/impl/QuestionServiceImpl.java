package com.rosevvi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosevvi.dao.QuestionDao;
import com.rosevvi.domain.Comment;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.QuestionBridge;
import com.rosevvi.domain.Type;
import com.rosevvi.dto.QuestionDto;
import com.rosevvi.service.CommentService;
import com.rosevvi.service.QuestionBridgeService;
import com.rosevvi.service.QuestionService;
import com.rosevvi.service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: rosevvi
 * @date: 2023/3/27 10:14
 * @version: 1.0
 * @description:
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionDao,Question> implements QuestionService {

    @Autowired
    private QuestionBridgeService questionBridgeService;

    @Autowired
    private TypeService typeService;

    private CommentService commentService;

    /**
     * 懒加载 解决循环依赖问题
     * @param commentService
     */
    @Autowired
    @Lazy
    public void ApplicationComment(CommentService commentService){
        this.commentService=commentService;
    }



    @Override
    public Long saveQuestionAndType(@RequestBody QuestionDto questionDto) {
        //拿到questionDto先把其分成两个  一个Question  一个typeList
        Question question=new Question();
        BeanUtils.copyProperties(questionDto,question,"types");
        List<Type> types = questionDto.getTypes();
        //先添加问题
        this.save(question);
        //在添加QuestionBridge
        List<QuestionBridge> bridgeList=new ArrayList<>();
        types.forEach(item->{
            bridgeList.add(new QuestionBridge(question.getId(),item.getId()));
        });
        questionBridgeService.saveBatch(bridgeList);

        return question.getId();
    }

    /**
     * 随机查询十条问题  并查询出他们对应的回答数量
     * @return
     */
    @Override
    public List<Question> pageForAnswerNum() {

        long count = this.count();
        long randomCount= (long)(Math.random()*count);
        if (randomCount>count-10){
            randomCount = count-10;
        }
        LambdaQueryWrapper<Question> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.last("limit "+String.valueOf(randomCount)+",10");
        return this.list(queryWrapper);
    }

    /**
     * 获取单个问题详情
     * @param questionId
     * @return
     */
    @Override
    public QuestionDto getOneQuestion(Long questionId){
        Question question = this.getById(questionId);
        QuestionDto dto=new QuestionDto();
        //将查询到的单个问题复制给dto
        BeanUtils.copyProperties(question,dto);
        //查询问题分类桥接
        LambdaQueryWrapper<QuestionBridge> bridgeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        bridgeLambdaQueryWrapper.eq(QuestionBridge::getQuestionId,question.getId());
        //查询问题分类桥接
        List<QuestionBridge> questionBridgeList = questionBridgeService.list(bridgeLambdaQueryWrapper);
        //将问题桥接类集合中的typeid取出
        List<Long> typeIdList = questionBridgeList.stream().map(QuestionBridge::getTypeId).collect(Collectors.toList());
        //根据typeid集合对问题分类type进行查询
        LambdaQueryWrapper<Type> typeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        typeLambdaQueryWrapper.in(Type::getId,typeIdList);
        List<Type> list = typeService.list(typeLambdaQueryWrapper);
        dto.setTypes(list);
        return dto;
    }

    /**
     * 根据分类获取问题列表
     * @param id
     * @return
     */
    @Override
    public List<Question> getQuestionByType(Long id) {
        //先用typeid获取QuestionBridge
        LambdaQueryWrapper<QuestionBridge> bridgeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        bridgeLambdaQueryWrapper.eq(QuestionBridge::getTypeId,id);
        List<QuestionBridge> questionBridges = questionBridgeService.list(bridgeLambdaQueryWrapper);
        //将问题分类桥接集合中的问题id拿出来
        List<Long> questionIdList = questionBridges.stream().map(QuestionBridge::getQuestionId).collect(Collectors.toList());
        //利用questionId去查找问题
        List<Question> questions = this.listByIds(questionIdList);
        return questions;
    }

    /**
     * 删除指定问题
     * @param id
     * @return
     */
    @Override
    public void deleteByQuestionId(Long id) {
        //删除指定问题时要把问题关联的评论 和 问题关联的分类桥接 删除
        //先根据删除问题评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper=new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getQuestionId,id);
        commentService.remove(commentLambdaQueryWrapper);
        //再删除问题关联的分类桥接
        LambdaQueryWrapper<QuestionBridge> bridgeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        bridgeLambdaQueryWrapper.eq(QuestionBridge::getQuestionId,id);
        questionBridgeService.remove(bridgeLambdaQueryWrapper);
        //最后删除问题
        this.removeById(id);
    }

    /**
     * 更新问题信息
     * @param questionDto
     */
    @Override
    public void updateQuestion(QuestionDto questionDto) {
        //更新问题要把问题分类桥接表 一起更改
        Question question=new Question();
        BeanUtils.copyProperties(questionDto,question,"types");
        //先删除问题分类桥接表数据
        LambdaQueryWrapper<QuestionBridge> bridgeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        bridgeLambdaQueryWrapper.eq(QuestionBridge::getQuestionId,question.getId());
        questionBridgeService.remove(bridgeLambdaQueryWrapper);
        //先拿到quesitonDto里面的分类id
        List<Type> types = questionDto.getTypes();
        //再添加回questionBridge
        List<QuestionBridge> questionBridges=new ArrayList<>();
        types.forEach(item->{
            questionBridges.add(new QuestionBridge(question.getId(),item.getId()));
        });
        questionBridgeService.saveBatch(questionBridges);
        //最后再把问题更改
        this.updateById(question);
    }


}
