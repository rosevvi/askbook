package com.rosevvi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosevvi.config.BaseContext;
import com.rosevvi.domain.Collect;
import com.rosevvi.dto.CollectDto;
import com.rosevvi.service.CollectService;
import com.rosevvi.tools.Code;
import com.rosevvi.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/6 10:17
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/collect")
@Slf4j
public class CollectController {

    @Autowired
    private CollectService collectService;

    //获取当前用户的收藏
    @GetMapping
    public Result<CollectDto> getCollectByNowUser(){
        CollectDto dto = collectService.getCollectByNowUser();
        return Result.success(Code.OK,"查询成功",dto);
    }

    //收藏问题
    @PostMapping("/collectQuestion/{id}")
    public Result<String> collectQuestion(@PathVariable("id") Long id){
        collectService.collectQuestion(id);
        return Result.success(Code.OK,"添加收藏成功");
    }
    //收藏问题
    @PostMapping("/collectText/{id}")
    public Result<String> collectText(@PathVariable("id") Long id){
        collectService.collectText(id);
        return Result.success(Code.OK,"添加收藏成功");
    }
}
