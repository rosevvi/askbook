package com.rosevvi.Handler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosevvi.config.BaseContext;
import com.rosevvi.domain.Message;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.User;
import com.rosevvi.service.QuestionService;
import com.rosevvi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: rosevvi
 * @date: 2023/3/29 11:12
 * @version: 1.0
 * @description:
 */
@Service
@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler implements WebSocketHandler {

    private static UserService userService;

    private static QuestionService questionService;

    @Autowired
    public void setApplicationContext(QuestionService questionService) {
        MyWebSocketHandler.questionService = questionService;
    }

    // 在线用户列表 用户id和用户session
    private static final Map<String, WebSocketSession> users = new ConcurrentHashMap<>();

    //在线用户列表  用户session和用户id
    private static final Map<WebSocketSession, String> suser = new ConcurrentHashMap<>();

    // 问题对应的在线人数 问题id 和 在线人数
    private static final Map<Long, Long> questionUserList = new ConcurrentHashMap<>();

    // 房间聊天  房间号  用户session集合
    private static final Map<String, List<WebSocketSession>> questionHome = new ConcurrentHashMap<>();

    @Autowired
    public void setApplicationContext(UserService userService) {
        MyWebSocketHandler.userService = userService;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("成功建立websocket-spring连接");
        try {
            LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(Question::getIcon);
            List<Question> list = questionService.list(queryWrapper);
            list.forEach(item -> {
                questionUserList.put(item.getId(), 0L);
            });
            User user = userService.getById(BaseContext.getThreadLocal());
            if (user == null) {
                session.close();
                return;
            }
            users.put(BaseContext.getThreadLocal().toString(), session);
            suser.put(session, BaseContext.getThreadLocal().toString());
        } catch (IOException e) {
            e.printStackTrace();
            log.info("没有此用户");
        }
        log.info("当前连接人数:{}", users.size());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        synchronized (this) {
            log.info(message.getPayload() + "<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            JSONObject jsonObject = JSONObject.parseObject(message.getPayload());
            Message data = jsonObject.getObject("data", Message.class);
            data.setUserId(BaseContext.getThreadLocal());
            log.info("收到客户端消息：{}", message.getPayload());
            // 判断 flag是否未message  未message则说明是给用户发送消息
            if ("message".equals(data.getFlag())) {
                //代表给这个用户id发送消息
                sendMessageToUser(data.getToUserId().toString(), new TextMessage(jsonObject.toString()));
            }
            //判断消息flag是否威questionHomeMessage   是的话说明是向聊天室发送的消息
            if ("questionHomeMessage".equals(data.getFlag())) {
                List<WebSocketSession> list = questionHome.get(data.getQuestionId().toString());
                for (int i = 0; i < list.size(); i++) {
                    if (!list.get(i).isOpen()) {
                        list.remove(i);
                        continue;
                    }
                    try {
                        log.info(list.get(i) + "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                        log.info(list.size() + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + list.get(i).isOpen() + questionUserList.toString());
                        list.get(i).sendMessage(new TextMessage(jsonObject.toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + list.toString());
            }

            //判断消息flag是否为question
            if ("question".equals(data.getFlag())) {
                //问题推送
                sendMessageToAllUsers(new TextMessage(jsonObject.toString()));
                log.info("给所有人发");
            }
            // 判断消息的flag是否是 icon  如果是则代表 是首页的问题的在线人数
            if ("icon".equals(data.getFlag())) {
                //获取当前问题的在线讨论人数
                data.setMessage(JSONObject.toJSONString(questionUserList));
                sendMessageToAllUsers(new TextMessage(JSONObject.toJSONString(data)));
            }
            //判断消息的flag是否未questionUserSize，是则代表有用户访问问题页面  给对应的数据加1
            if ("questionUserSize".equals(data.getFlag())) {
                questionUserList.forEach((key, value) -> {
                    if (key.equals(data.getQuestionId())) {
                        questionUserList.put(key, value + 1);
                    }
                });
            }
            //判断flag是否威questionHome  如果是则代表进去了问题页面的聊天室
            if ("questionHome".equals(data.getFlag())) {
                List<WebSocketSession> list = questionHome.get(data.getQuestionId().toString());
                //如果list大小小于1则代表没有这个问题的聊天室
                if (list == null) {
                    //没有聊天室就创建一个聊天室
                    List<WebSocketSession> sessions = new ArrayList<>();
                    sessions.add(session);
                    //判断消息的flag是否未questionUserSize，是则代表有用户访问问题页面  给对应的数据加1
                    questionHome.put(data.getQuestionId().toString(), sessions);
                    log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + sessions.toString());
                    questionUserList.forEach((key, value) -> {
                        if (key.equals(data.getQuestionId())) {
                            questionUserList.put(key, value + 1);
                        }
                    });
                } else {
                    //有聊天室就把当前用户添加进去
                    list.add(session);
                }
            }
            //退出聊天室
            if ("questionRemoveHome".equals(data.getFlag())) {
                //从聊天室把当前用户移除
                List<WebSocketSession> list = questionHome.get(data.getQuestionId().toString());
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == session) {
                        list.remove(i);
                    }
                }
                questionUserList.forEach((key, value) -> {
                    if (key.equals(data.getQuestionId())) {
                        questionUserList.put(key, value - 1);
                    }
                });
            }
            questionUserList.forEach((key, value) -> {
                log.info("!!!!!" + key + ":" + value);
            });
            try {
                WebSocketMessage<?> webSocketMessageServer = new TextMessage("server:" + message);
                session.sendMessage(webSocketMessageServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        log.info("连接出错" + exception.getMessage());
        users.remove(BaseContext.getThreadLocal().toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("连接已关闭：" + status);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 发送信息给指定用户
     *
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String toUser, TextMessage message) {
        if (users.get(toUser) == null) {
            return false;
        }
        WebSocketSession session = users.get(toUser);
        log.info("sendMessage：{} ,msg：{}", session, message.getPayload());
        if (!session.isOpen()) {
            log.info("客户端:{},已断开连接，发送消息失败", toUser);
            return false;
        }
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.info("sendMessageToUser method error：{}", e);
            return false;
        }
        return true;
    }


    public void closeLink() {
        try {
            WebSocketSession session = users.get(BaseContext.getThreadLocal().toString());
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("【websocket】关闭出错");
        }
    }

    /**
     * 广播消息
     *
     * @param message
     * @return
     */
    public static boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<String> mchNos = users.keySet();
        WebSocketSession tosession = null;
        for (String mchNo : mchNos) {
            try {
                tosession = users.get(mchNo);
                if (tosession.isOpen()) {
                    tosession.sendMessage(message);
                }
            } catch (IOException e) {
                log.info("sendMessageToAllUsers method error：{}", e);
                allSendSuccess = false;
            }
        }
        return allSendSuccess;
    }


    public static Long getOnLineUserList(Long questionId) {
        return questionUserList.get(questionId);
    }

}
