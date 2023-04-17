package com.rosevvi.dto;

import com.rosevvi.domain.Question;
import com.rosevvi.domain.Type;
import lombok.Data;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/7 19:57
 * @version: 1.0
 * @description:
 */
@Data
public class QuestionDto extends Question {
    private List<Type> types;
}
