package com.rosevvi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosevvi.dao.TextBridgeDao;
import com.rosevvi.domain.TextBridge;
import com.rosevvi.service.TextBridgeService;
import org.springframework.stereotype.Service;

/**
 * @author: rosevvi
 * @date: 2023/4/3 16:28
 * @version: 1.0
 * @description:
 */
@Service
public class TextBridgeServiceImpl extends ServiceImpl<TextBridgeDao, TextBridge> implements TextBridgeService {
}
