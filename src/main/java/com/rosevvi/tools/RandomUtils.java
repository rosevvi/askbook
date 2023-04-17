package com.rosevvi.tools;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author: rosevvi
 * @date: 2023/4/14 21:19
 * @version: 1.0
 * @description:
 */
public class RandomUtils {
    private static final Random random=new Random();

    private static final DecimalFormat four=new DecimalFormat("0000");

    public static String getFourBitRandom(){
        return four.format(random.nextInt(10000));
    }
}
