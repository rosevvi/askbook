package com.rosevvi.controller;

import com.rosevvi.Handler.MyWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: rosevvi
 * @date: 2023/3/30 9:39
 * @version: 1.0
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/websocket")
public class WebSocketController {

    @Autowired
    private MyWebSocketHandler socketHandler;

    @RequestMapping("/push")
    public void pushMessage(){
        log.info("啦啦啦啦啦啦啦啦啦啦啦啦啦啦");
    }
}
