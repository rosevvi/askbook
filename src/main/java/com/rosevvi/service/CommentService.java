package com.rosevvi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosevvi.domain.Comment;
import com.rosevvi.dto.CommentNewsDto;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/8 17:08
 * @version: 1.0
 * @description:
 */
public interface CommentService extends IService<Comment> {

    public List<CommentNewsDto> getWhoCommentMe();

}
