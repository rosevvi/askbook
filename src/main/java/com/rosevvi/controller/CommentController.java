package com.rosevvi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosevvi.config.BaseContext;
import com.rosevvi.domain.Comment;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import com.rosevvi.domain.User;
import com.rosevvi.dto.CommentNewsDto;
import com.rosevvi.service.CommentService;
import com.rosevvi.service.QuestionService;
import com.rosevvi.service.TextService;
import com.rosevvi.service.UserService;
import com.rosevvi.tools.Code;
import com.rosevvi.tools.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/8 17:09
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private UserService userService;

    @Autowired
    private TextService textService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @PostMapping("/toText")
    public Result<String> saveByText(@RequestBody Comment comment){
        //先通过评论的文章id找到作者
        Text text = textService.getById(comment.getTextId());
        comment.setUserId(BaseContext.getThreadLocal());
        comment.setToUserId(text.getCreateUser());
        commentService.save(comment);
        return Result.success(Code.OK,"评论成功",null);
    }


    @PostMapping("/toQuestion")
    public Result<String> saveByQuestion(@RequestBody Comment comment){
        //先通过评论的问题id找到作者
        Question question = questionService.getById(comment.getQuestionId());
        comment.setUserId(BaseContext.getThreadLocal());
        comment.setToUserId(question.getCreateUser());
        commentService.save(comment);
        return Result.success(Code.OK,"评论成功",null);
    }

    /**
     * 查询问题评论
     * @param questionId
     * @return
     */
    @GetMapping("/getQuestionComment/{id}")
    public Result<List<CommentNewsDto>> getCommentByQuestion(@PathVariable("id") Long questionId){
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper=new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getQuestionId,questionId);
        commentLambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> list = commentService.list(commentLambdaQueryWrapper);
        List<CommentNewsDto> commentNewsDtos=new ArrayList<>();
        list.forEach(item->{
            CommentNewsDto commentNewsDto=new CommentNewsDto();
            User user = userService.getById(item.getUserId());
            user.setPassword(null);
            BeanUtils.copyProperties(item,commentNewsDto);
            commentNewsDto.setUser(user);
            commentNewsDtos.add(commentNewsDto);
        });
        return Result.success(Code.OK,"查询成功",commentNewsDtos);
    }


    /**
     * 查询问题评论
     * @param textId
     * @return
     */
    @GetMapping("/getTextComment/{id}")
    public Result<List<CommentNewsDto>> getCommentByText(@PathVariable("id") Long textId){
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper=new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getTextId,textId);
        commentLambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> list = commentService.list(commentLambdaQueryWrapper);
        List<CommentNewsDto> commentNewsDtos=new ArrayList<>();
        list.forEach(item->{
            CommentNewsDto commentNewsDto=new CommentNewsDto();
            User user = userService.getById(item.getUserId());
            user.setPassword(null);
            BeanUtils.copyProperties(item,commentNewsDto);
            commentNewsDto.setUser(user);
            commentNewsDtos.add(commentNewsDto);
        });
        return Result.success(Code.OK,"查询成功",commentNewsDtos);
    }


}
