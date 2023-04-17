package com.rosevvi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosevvi.domain.UserLike;
import com.rosevvi.dto.UserLikeNewsDto;
import com.rosevvi.dto.UserLikeDto;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/6 11:27
 * @version: 1.0
 * @description:
 */
public interface UserLikeService extends IService<UserLike> {
    //获取当前用户点赞的
    public UserLikeDto getLikeByNowUser();

    //获取给当前用户点赞的
    public List<UserLikeNewsDto> getLikeByNowToUser();

    public void likeQuestion(Long id);


    public void likeText(Long id);

}
