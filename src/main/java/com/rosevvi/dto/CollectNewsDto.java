package com.rosevvi.dto;

import com.rosevvi.domain.Collect;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import com.rosevvi.domain.User;
import lombok.Data;

/**
 * @author: rosevvi
 * @date: 2023/4/9 21:17
 * @version: 1.0
 * @description:
 */
@Data
public class CollectNewsDto extends Collect {

    private User user;

    private Question question;

    private Text text;

    private String msg;
}
