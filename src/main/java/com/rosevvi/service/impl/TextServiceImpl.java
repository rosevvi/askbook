package com.rosevvi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosevvi.config.BaseContext;
import com.rosevvi.dao.TextDao;
import com.rosevvi.domain.Comment;
import com.rosevvi.domain.Text;
import com.rosevvi.domain.TextBridge;
import com.rosevvi.domain.Type;
import com.rosevvi.dto.TextDto;
import com.rosevvi.service.CommentService;
import com.rosevvi.service.TextBridgeService;
import com.rosevvi.service.TextService;
import com.rosevvi.service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: rosevvi
 * @date: 2023/4/1 13:08
 * @version: 1.0
 * @description:
 */
@Service
@Slf4j
public class TextServiceImpl extends ServiceImpl<TextDao, Text> implements TextService {

    @Autowired
    private TextBridgeService textBridgeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TypeService typeService;

    /**
     * 添加文章
     * @param textDto
     */
    @Override
    public Long saveTextAndType(@RequestBody TextDto textDto) {
        //拿到textDto先把他分为两个  text 和 typeList
        Text text=new Text();
        BeanUtils.copyProperties(textDto,text,"types");
        //先保存text  保存后text中的id就填充上了
        this.save(text);
        //再保存textBridge
        //此案获取到type集合
        List<Type> types = textDto.getTypes();
        //遍历添加到textBridge
        List<TextBridge> bridgeList=new ArrayList<>();
        types.forEach(item->{
            bridgeList.add(new TextBridge(text.getId(),item.getId()));
        });
        //保存textBridge
        textBridgeService.saveBatch(bridgeList);

        return text.getId();
    }

    /**
     * 根据分类查找文章
     * @param id
     * @return
     */
    @Override
    public List<Text> getTextByType(Long id) {
        //先利用id 查找TextBridge
        LambdaQueryWrapper<TextBridge> bridgeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        bridgeLambdaQueryWrapper.eq(TextBridge::getTypeId,id);
        List<TextBridge> textBridges = textBridgeService.list(bridgeLambdaQueryWrapper);
        //获取到textBridges中的textId
        List<Long> textIds = textBridges.stream().map(TextBridge::getTextId).collect(Collectors.toList());
        //利用textIds查找文章
        List<Text> texts = this.listByIds(textIds);
        return texts;
    }

    /**
     * 根据id删除文章
     * @param id
     */
    @Override
    public void deleteTextById(Long id) {
        //删除文章时 同时要删除 文章分类桥接 和文章评论、
        //先删除文章分类桥接
        LambdaQueryWrapper<TextBridge> textBridgeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        textBridgeLambdaQueryWrapper.eq(TextBridge::getTextId,id);
        textBridgeService.remove(textBridgeLambdaQueryWrapper);
        //再删除文章评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper=new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getTextId,id);
        commentService.remove(commentLambdaQueryWrapper);
        //最后删除文章
        this.removeById(id);
    }

    /**
     * 更新文章
     * @param textDto
     */
    @Override
    public void updateText(TextDto textDto) {
        Text text=new Text();
        //拿到textDto先把它分成两份 即更新文章的时候 文章的分类桥接也要更新
        BeanUtils.copyProperties(textDto,text,"types");
        List<Type> types = textDto.getTypes();
        //先更新文章分类桥接
        //先把原文章分类桥接删除
        LambdaQueryWrapper<TextBridge> textBridgeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        textBridgeLambdaQueryWrapper.eq(TextBridge::getTextId,text.getId());
        textBridgeService.remove(textBridgeLambdaQueryWrapper);
        //再把新文章分类添加回去
        List<TextBridge> textBridges=new ArrayList<>();
        types.forEach(item->{
            textBridges.add(new TextBridge(text.getId(),item.getId()));
        });
        textBridgeService.saveBatch(textBridges);
        //最后把文章也更新
        this.updateById(text);
    }

    /**
     * 根据id获取单个文章
     * @param id
     * @return
     */
    @Override
    public TextDto getOneTextById(Long id) {
        TextDto textDto=new TextDto();
        //查询文章要查询两个表  一个文章表text  一个文章分类表textBridge
        Text text = this.getById(id);
        //将text复制给textDto
        BeanUtils.copyProperties(text,textDto);
        //再查找textbridge来获取文章分类id
        LambdaQueryWrapper<TextBridge> textBridgeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        textBridgeLambdaQueryWrapper.eq(TextBridge::getTextId,id);
        List<TextBridge> textBridges = textBridgeService.list(textBridgeLambdaQueryWrapper);
        //拿到textBridges里面的分类id
        List<Long> typeIds = textBridges.stream().map(TextBridge::getTypeId).collect(Collectors.toList());
        //再去查找type表
        LambdaQueryWrapper<Type> typeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        typeLambdaQueryWrapper.in(Type::getId,typeIds);
        List<Type> types = typeService.list(typeLambdaQueryWrapper);
        //给dto赋值
        textDto.setTypes(types);
        return textDto;
    }
}
