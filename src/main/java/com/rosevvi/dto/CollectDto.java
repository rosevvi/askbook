package com.rosevvi.dto;

import com.rosevvi.domain.Collect;
import com.rosevvi.domain.Question;
import com.rosevvi.domain.Text;
import lombok.Data;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/6 10:23
 * @version: 1.0
 * @description:
 */
@Data
public class CollectDto  {

    private List<Question> questions;

    private List<Text> texts;


}
