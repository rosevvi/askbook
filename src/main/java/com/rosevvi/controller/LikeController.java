package com.rosevvi.controller;

import com.rosevvi.dto.UserLikeDto;
import com.rosevvi.service.UserLikeService;
import com.rosevvi.tools.Code;
import com.rosevvi.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: rosevvi
 * @date: 2023/4/6 11:30
 * @version: 1.0
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private UserLikeService userLikeService;

    @GetMapping
    public Result<UserLikeDto> getLikeByNowUser(){
        UserLikeDto dto = userLikeService.getLikeByNowUser();
        return Result.success(Code.OK,"查询成功",dto);
    }

    //添加喜欢问题
    @PostMapping("/likeQuestion/{id}")
    public Result<String> likeQuestion(@PathVariable("id") Long id){
        userLikeService.likeQuestion(id);
        return Result.success(Code.OK,"添加喜欢成功");
    }

    //添加喜欢文章
    @PostMapping("/likeText/{id}")
    public Result<String> likeText(@PathVariable("id") Long id){
        userLikeService.likeText(id);
        return Result.success(Code.OK,"添加喜欢成功");
    }

}
