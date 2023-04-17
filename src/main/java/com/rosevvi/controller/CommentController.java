package com.rosevvi.controller;

import com.rosevvi.config.BaseContext;
import com.rosevvi.domain.Comment;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import com.rosevvi.service.CommentService;
import com.rosevvi.service.QuestionService;
import com.rosevvi.service.TextService;
import com.rosevvi.tools.Code;
import com.rosevvi.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
