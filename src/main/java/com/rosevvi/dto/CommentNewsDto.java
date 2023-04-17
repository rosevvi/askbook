package com.rosevvi.dto;

import com.rosevvi.domain.Comment;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import com.rosevvi.domain.User;
import lombok.Data;

/**
 * @author: rosevvi
 * @date: 2023/4/10 9:23
 * @version: 1.0
 * @description:
 */
@Data
public class CommentNewsDto extends Comment {
    private User user;

    private Question question;

    private Text text;

    private String msg;
}
