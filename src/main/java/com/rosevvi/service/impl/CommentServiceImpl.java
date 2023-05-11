package com.rosevvi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosevvi.config.BaseContext;
import com.rosevvi.dao.CommentDao;
import com.rosevvi.domain.Comment;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import com.rosevvi.domain.User;
import com.rosevvi.dto.CommentNewsDto;
import com.rosevvi.service.CommentService;
import com.rosevvi.service.QuestionService;
import com.rosevvi.service.TextService;
import com.rosevvi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/8 17:08
 * @version: 1.0
 * @description:
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    private QuestionService questionService;

    private TextService textService;

    private UserService userService;

    /**
     * 懒加载   解决循环依赖问题   让其不在容器初始化时就创建bean  而是啥时候用到啥时候就创建
     * @param questionService
     */
    @Autowired
    @Lazy
    public void ApplicationQuestion(QuestionService questionService){
        this.questionService = questionService;
    }

    @Autowired
    @Lazy
    public void ApplicationText(TextService textService){
        this.textService=textService;
    }

    @Autowired
    @Lazy
    public void ApplicationUser(UserService userService){
        this.userService=userService;
    }

    /**
     * 谁评论了
     * @return
     */
    @Override
    public List<CommentNewsDto> getWhoCommentMe() {
        //查询谁评论的
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper=new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getToUserId, BaseContext.getThreadLocal());
        commentLambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> list = this.list(commentLambdaQueryWrapper);
        if (list.size()<1){
            return null;
        }
        //组装成newdto集合
        List<CommentNewsDto> commentNewsDtos =new ArrayList<>();

        list.stream().forEach(item->{
            CommentNewsDto commentNewsDto =new CommentNewsDto();
            BeanUtils.copyProperties(item, commentNewsDto);
            //先查用户
            User user = userService.getById(item.getUserId());
            user.setPassword(null);
            //在查文章
            Text text = textService.getById(item.getTextId());
            //在查问题
            Question question = questionService.getById(item.getQuestionId());
            commentNewsDto.setUser(user);
            commentNewsDto.setText(text);
            commentNewsDto.setQuestion(question);

            commentNewsDtos.add(commentNewsDto);
        });
        return commentNewsDtos;
    }



}
