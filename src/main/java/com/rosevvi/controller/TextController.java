package com.rosevvi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosevvi.config.BaseContext;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import com.rosevvi.domain.Type;
import com.rosevvi.dto.TextDto;
import com.rosevvi.service.TextService;
import com.rosevvi.service.TypeService;
import com.rosevvi.tools.Code;
import com.rosevvi.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/1 13:09
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/text")
@Slf4j
public class TextController {
    @Autowired
    private TextService textService;

    @Autowired
    private TypeService typeService;

    /**
     * 获取文章分类
     * @return
     */
    @GetMapping("/type")
    public Result<List<Type>> getType(){
        List<Type> list = typeService.list();
        return Result.success(Code.OK,"查询成功",list);
    }

    /**
     * 获取文章列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<Text>> list(){
        List<Text> list = textService.list();
        return Result.success(Code.OK,"查询成功",list);
    }

    /**
     * 添加文章
     * @param textDto
     * @return
     */
    @PostMapping()
    public Result<Long> save(@RequestBody TextDto textDto){

//        textService.save(text);
        Long id = textService.saveTextAndType(textDto);
        return Result.success(Code.OK,"添加成功",id);
    }

    /**
     * 获取10个随机文章
     * @return
     */
    @GetMapping("/pageRandom")
    public Result<List<Text>> pageRandom(){
        long count = textService.count();
        long randomCount= (long)(Math.random()*count);
        if (randomCount>count-10){
            randomCount = count-10;
        }
        LambdaQueryWrapper<Text> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.last("limit "+String.valueOf(randomCount)+",10");
        List<Text> list = textService.list(queryWrapper);
        return Result.success(Code.OK,"查询成功",list);
    }

    /**
     * 获取当前用户文章
     * @return
     */
    @GetMapping
    public Result<List<Text>> getTextByNowUser(){
        LambdaQueryWrapper<Text> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Text::getCreateUser, BaseContext.getThreadLocal());
        List<Text> list = textService.list(queryWrapper);
        if (list.size()<1){
            return Result.error(Code.OK,"文章为空");
        }
        return Result.success(Code.OK,"查询成功",list);
    }

    /**
     * 根据文章分类查询文章
     * @param id
     * @return
     */
    @GetMapping("/byType/{id}")
    public Result<List<Text>> getTextByType(@PathVariable("id") Long id){
        List<Text> texts = textService.getTextByType(id);
        return Result.success(Code.OK,"查找成功",texts);
    }

    /**
     * 根据id删除文章
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteTextById(@PathVariable("id") Long id){
        textService.deleteTextById(id);
        return Result.success(Code.OK,"删除成功",null);
    }

    /**
     * 更新文章
     * @param textDto
     * @return
     */
    @PutMapping
    public Result<String> updateText(@RequestBody TextDto textDto){
        textService.updateText(textDto);
        return Result.success(Code.OK,"更新成功",null);
    }

    /**
     * 根据id查找单个文章
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<TextDto> getOneText(@PathVariable("id") Long id){
        TextDto textDto = textService.getOneTextById(id);
        return Result.success(Code.OK,"查找成功",textDto);
    }
}
