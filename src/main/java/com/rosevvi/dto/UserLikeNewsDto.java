package com.rosevvi.dto;

import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import com.rosevvi.domain.User;
import com.rosevvi.domain.UserLike;
import lombok.Data;

/**
 * @author: rosevvi
 * @date: 2023/4/9 19:33
 * @version: 1.0
 * @description:
 */
@Data
public class UserLikeNewsDto extends UserLike {

    private User user;

    private Question question;

    private Text text;

    private String msg;


}
