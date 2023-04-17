package com.rosevvi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosevvi.domain.Text;
import com.rosevvi.dto.TextDto;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/1 13:07
 * @version: 1.0
 * @description:
 */
public interface TextService extends IService<Text> {

    public Long saveTextAndType(TextDto textDto);

    public List<Text> getTextByType(Long id);

    public void deleteTextById(Long id);

    public void updateText(TextDto textDto);

    public TextDto getOneTextById(Long id);
}
