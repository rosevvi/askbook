package com.rosevvi.config;

import com.rosevvi.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author: rosevvi
 * @date: 2023/3/24 18:08
 * @version: 1.0
 * @description:
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/");
    }

    /**
     * 扩展mvc框架的消息转换器 解决long类型的id精度丢失问题
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        //创建消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将java对象转换为json
        converter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器追加到mvc的框架容器中并设置优先级
        converters.add(0,converter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginCheckInterceptor());
    }

    @Bean
    public LoginCheckInterceptor getLoginCheckInterceptor(){
        return new LoginCheckInterceptor();
    }

}
