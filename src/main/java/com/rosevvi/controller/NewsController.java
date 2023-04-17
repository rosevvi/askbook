package com.rosevvi.controller;

import com.rosevvi.dto.CollectNewsDto;
import com.rosevvi.dto.CommentNewsDto;
import com.rosevvi.dto.UserLikeNewsDto;
import com.rosevvi.service.CollectService;
import com.rosevvi.service.CommentService;
import com.rosevvi.service.UserLikeService;
import com.rosevvi.tools.Code;
import com.rosevvi.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/8 16:59
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/news")
public class NewsController {


    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/whoLikeMe")
    public Result<List<UserLikeNewsDto>> getWhoLikeMe(){
        //查询给谁点赞id为当前用户的点赞列表
        List<UserLikeNewsDto> userLikeNewsDtos = userLikeService.getLikeByNowToUser();
        return Result.success(Code.OK,"查询成功", userLikeNewsDtos);
    }

    @GetMapping("/whoCollectMe")
    public Result<List<CollectNewsDto>> getWhoCollectMe(){
        List<CollectNewsDto> collectNewsDtoList = collectService.getCollectByToUser();
        return Result.success(Code.OK,"查询成功",collectNewsDtoList);
    }

    @GetMapping("/whoCommentMe")
    public Result<List<CommentNewsDto>> getWhoCommentMe(){
        List<CommentNewsDto> newsDtoList = commentService.getWhoCommentMe();
        return Result.success(Code.OK,"查询成功",newsDtoList);
    }
}
