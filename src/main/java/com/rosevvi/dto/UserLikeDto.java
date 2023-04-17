package com.rosevvi.dto;

import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import lombok.Data;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/6 11:33
 * @version: 1.0
 * @description:
 */
@Data
public class UserLikeDto {

    private List<Question> questions;

    private List<Text> texts;
}
