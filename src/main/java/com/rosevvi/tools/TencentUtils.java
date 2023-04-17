package com.rosevvi.tools;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: rosevvi
 * @date: 2023/4/14 21:57
 * @version: 1.0
 * @description:
 */
@Component
public class TencentUtils implements InitializingBean {

    @Value("${tencent.sms.secretId}")
    public String secretId;
    @Value("${tencent.sms.secretKey}")
    public String secretKey;
    @Value("${tencent.cos.buckerName}")
    public String buckerName;
    @Value("${tencent.cos.region}")
    public String region;
    @Value("${tencent.cos.url}")
    public String url;

    public static  String SECRET_ID;
    public static  String SECRET_KEY;
    public static  String BUCKER_NAME;
    public static  String REGION;
    public static  String URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        SECRET_ID=secretId;
        SECRET_KEY=secretKey;
        BUCKER_NAME=buckerName;
        REGION=region;
        URL=url;
    }
}
