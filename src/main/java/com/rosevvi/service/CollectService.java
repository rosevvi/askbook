package com.rosevvi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosevvi.domain.Collect;
import com.rosevvi.dto.CollectDto;
import com.rosevvi.dto.CollectNewsDto;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/6 10:16
 * @version: 1.0
 * @description:
 */
public interface CollectService extends IService<Collect> {

    public CollectDto getCollectByNowUser();

    //获取谁收藏了
    public List<CollectNewsDto> getCollectByToUser();

    public void collectQuestion(Long id);

    public void collectText(Long id);
}
