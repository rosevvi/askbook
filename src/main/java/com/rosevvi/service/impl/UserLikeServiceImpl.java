package com.rosevvi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosevvi.config.BaseContext;
import com.rosevvi.dao.UserLikeDao;
import com.rosevvi.domain.User;
import com.rosevvi.domain.UserLike;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import com.rosevvi.dto.UserLikeNewsDto;
import com.rosevvi.dto.UserLikeDto;
import com.rosevvi.service.UserLikeService;
import com.rosevvi.service.QuestionService;
import com.rosevvi.service.TextService;
import com.rosevvi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: rosevvi
 * @date: 2023/4/6 11:27
 * @version: 1.0
 * @description:
 */
@Slf4j
@Service
public class UserLikeServiceImpl extends ServiceImpl<UserLikeDao, UserLike> implements UserLikeService {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TextService textService;

    @Autowired
    private UserService userService;

    /**
     * 查询当前用户的点赞
     * @return
     */
    @Override
    public UserLikeDto getLikeByNowUser() {
        LambdaQueryWrapper<UserLike> likeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        likeLambdaQueryWrapper.eq(UserLike::getUserId, BaseContext.getThreadLocal());
        List<UserLike> list = this.list(likeLambdaQueryWrapper);
        if (list.size() < 1){
            return null;
        }
        List<Long> questionIds = list.stream().map(UserLike::getQuestionId).collect(Collectors.toList());
        List<Long> textIds = list.stream().map(UserLike::getTextId).collect(Collectors.toList());
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper=new LambdaQueryWrapper<>();
        questionLambdaQueryWrapper.in(Question::getId,questionIds);
        LambdaQueryWrapper<Text> textLambdaQueryWrapper=new LambdaQueryWrapper<>();
        textLambdaQueryWrapper.in(Text::getId,textIds);
        List<Question> questionList = questionService.list(questionLambdaQueryWrapper);
        List<Text> textList = textService.list(textLambdaQueryWrapper);
        UserLikeDto userLikeDto =new UserLikeDto();
        userLikeDto.setQuestions(questionList);
        userLikeDto.setTexts(textList);

        return userLikeDto;
    }

    /**
     * 给当前用户点赞的
     * @return
     */
    @Override
    public List<UserLikeNewsDto> getLikeByNowToUser() {
        //查询谁点赞的
        LambdaQueryWrapper<UserLike> userLikeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        userLikeLambdaQueryWrapper.eq(UserLike::getToUserId,BaseContext.getThreadLocal());
        userLikeLambdaQueryWrapper.orderByDesc(UserLike::getCreateTime);
        List<UserLike> list = this.list(userLikeLambdaQueryWrapper);
        if(list.size() < 1){
            return null;
        }
        //组装成newdto集合
        List<UserLikeNewsDto> userLikeNewsDtos =new ArrayList<>();

        list.stream().forEach(item->{
            UserLikeNewsDto userLikeNewsDto =new UserLikeNewsDto();
            BeanUtils.copyProperties(item, userLikeNewsDto);
            //先查用户
            User user = userService.getById(item.getUserId());
            user.setPassword(null);
            //在查文章
            Text text = textService.getById(item.getTextId());
            //在查问题
            Question question = questionService.getById(item.getQuestionId());
            userLikeNewsDto.setUser(user);
            userLikeNewsDto.setText(text);
            userLikeNewsDto.setQuestion(question);
            //判断问题是否为null  为空说名点赞的是文章
            if (question == null){
                userLikeNewsDto.setMsg("点赞了你的文章");
            }else {
                userLikeNewsDto.setMsg("点赞了你的问题");
            }
            userLikeNewsDtos.add(userLikeNewsDto);
        });
        return userLikeNewsDtos;
    }


    /**
     * 添加喜欢问题
     */
    @Override
    public void likeQuestion(Long id) {
        //根据id查找当前问题
        Question question = questionService.getById(id);
        //根据问题中的作者id 像点赞表插入记录
        UserLike userLike=new UserLike();
        userLike.setQuestionId(id);
        userLike.setUserId(BaseContext.getThreadLocal());
        userLike.setToUserId(question.getCreateUser());
        this.save(userLike);
    }

    /**
     * 添加喜欢文章
     */
    @Override
    public void likeText(Long id) {
        //根据id查找当前问题
        Text text = textService.getById(id);
        //根据问题中的作者id 像点赞表插入记录
        UserLike userLike=new UserLike();
        userLike.setTextId(id);
        userLike.setUserId(BaseContext.getThreadLocal());
        userLike.setToUserId(text.getCreateUser());
        this.save(userLike);
    }
}
