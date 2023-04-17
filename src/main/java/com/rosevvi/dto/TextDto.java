package com.rosevvi.dto;

import com.rosevvi.domain.Text;
import com.rosevvi.domain.Type;
import lombok.Data;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/4/7 21:50
 * @version: 1.0
 * @description:
 */
@Data
public class TextDto extends Text {

    private List<Type> types;
}
