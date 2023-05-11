package com.rosevvi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosevvi.config.BaseContext;
import com.rosevvi.dao.CollectDao;
import com.rosevvi.domain.Collect;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import com.rosevvi.domain.User;
import com.rosevvi.dto.CollectDto;
import com.rosevvi.dto.CollectNewsDto;
import com.rosevvi.service.CollectService;
import com.rosevvi.service.QuestionService;
import com.rosevvi.service.TextService;
import com.rosevvi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/6 10:16
 * @version: 1.0
 * @description:
 */
@Service
@Slf4j
public class CollectServiceImpl extends ServiceImpl<CollectDao, Collect> implements CollectService {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TextService textService;

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户收藏的
     * @return
     */
    @Override
    public CollectDto getCollectByNowUser() {
        LambdaQueryWrapper<Collect> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId, BaseContext.getThreadLocal());
        List<Collect> list = this.list(queryWrapper);
        if (list.size()<1){
            return null;
        }
        List<Question> questions=new ArrayList<>();
        List<Text> texts=new ArrayList<>();
        list.forEach(item->{
            if (item.getQuestionId() != null){
                Question question = questionService.getById(item.getQuestionId());
                if (question != null){
                    questions.add(question);
                }
            }
            if (item.getTextId()!=null){
                Text text = textService.getById(item.getTextId());
                if (text != null){
                    texts.add(text);
                }
            }
        });
        System.err.println(questions);
        System.err.println(texts);
        CollectDto collectDto=new CollectDto();
        collectDto.setQuestions(questions);
        collectDto.setTexts(texts);
        System.err.println(collectDto);
        return collectDto;
    }


    /**
     * 获取谁收藏了用户的
     * @return
     */
    @Override
    public List<CollectNewsDto> getCollectByToUser() {
        //查询谁收藏的
        LambdaQueryWrapper<Collect> collectLambdaQueryWrapper=new LambdaQueryWrapper<>();
        collectLambdaQueryWrapper.eq(Collect::getToUserId,BaseContext.getThreadLocal());
        collectLambdaQueryWrapper.orderByDesc(Collect::getCreateTime);
        List<Collect> list = this.list(collectLambdaQueryWrapper);
        if (list.size() <1){
            return null;
        }
        //组装成newdto集合
        List<CollectNewsDto> collectNewsDtos =new ArrayList<>();

        list.stream().forEach(item->{
            CollectNewsDto collectNewsDto =new CollectNewsDto();
            BeanUtils.copyProperties(item, collectNewsDto);
            //先查用户
            User user = userService.getById(item.getUserId());
            user.setPassword(null);
            //在查文章
            Text text = textService.getById(item.getTextId());
            //在查问题
            Question question = questionService.getById(item.getQuestionId());
            collectNewsDto.setUser(user);
            collectNewsDto.setText(text);
            collectNewsDto.setQuestion(question);
            //判断问题是否为null  为空说名收藏的是文章
            if (question == null){
                collectNewsDto.setMsg("收藏了你的文章");
            }else {
                collectNewsDto.setMsg("收藏了你的问题");
            }
            collectNewsDtos.add(collectNewsDto);
        });
        return collectNewsDtos;
    }

    /**
     * 收藏问题
     * @param id
     */
    @Override
    public void collectQuestion(Long id) {
        //根据问题id查询到问题信息
        Question question = questionService.getById(id);
        //拼接封装成一个collect对象
        Collect collect=new Collect();
        collect.setUserId(BaseContext.getThreadLocal());
        collect.setQuestionId(id);
        collect.setToUserId(question.getCreateUser());
        this.save(collect);
    }

    /**
     * 收藏文章
     * @param id
     */
    @Override
    public void collectText(Long id) {
        //根据问题id查询到文章信息
        Text text = textService.getById(id);
        //拼接封装成一个collect对象
        Collect collect=new Collect();
        collect.setUserId(BaseContext.getThreadLocal());
        collect.setTextId(id);
        collect.setToUserId(text.getCreateUser());
        this.save(collect);
    }
}