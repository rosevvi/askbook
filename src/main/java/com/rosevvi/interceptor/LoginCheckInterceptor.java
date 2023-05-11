package com.rosevvi.interceptor;

import com.rosevvi.config.BaseContext;
import com.rosevvi.tools.JsonWebToken;
import com.rosevvi.tools.TokenList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

/**
 * @author: rosevvi
 * @date: 2023/4/7 8:56
 * @version: 1.0
 * @description:
 */
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    public static final AntPathMatcher ANT_PATH_MATCHER=new AntPathMatcher();

    public static final JsonWebToken jsonWebToken=new JsonWebToken();;


    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取请求路径
        String uri = request.getRequestURI();
        log.info("请求路径："+uri);
        //要放行的路径
        String[] urls={
                "/",
                "/index.html",
                "/%E8%B7%B3%E4%B8%80%E8%B7%B3.html",
                "/跳一跳.html",
                "/vite.svg",
                "/assets/**",
                "/mota/**",
                "/pintu/**",
                "/qb.svg",
                "/user/login",
                "/user/register",
                "/user/verifyCode",
                "/websocket/**",
        };
        if ("/".equals(uri)||"/index".equals(uri)){
            response.sendRedirect("/index.html");
            return false;
        }

        if (uri.contains("/mota/imgs/%")){
            String two = URLDecoder.decode(uri.substring(uri.indexOf("%"),uri.indexOf(".")));
            String three = uri.substring(uri.indexOf("."));
            String end = two+three;
            request.getRequestDispatcher("/mota/imgs/"+end).forward(request,response);
        }


        //判断是否需要放行
        if (isCheck(urls,uri)){
            log.info("放行"+uri);
            return true;
        }
        //判断用户是否携带token
        String token = request.getHeader("Authorization");
        log.info(token);

        //判断token是否在黑名单
        if (TokenList.tokens.stream().anyMatch(tokens -> token.equals(tokens))){
            return false;
        }

        //查询redis中是否有对应的token
        Long id = null;
        try {
            id = jsonWebToken.getCalim(token);
        } catch (Exception e) {
            return false;
        }
        if (null != redisTemplate.opsForValue().get(id.toString())){
            BaseContext.setThreadLocal(id);
            redisTemplate.opsForValue().set(id.toString(),token,5, TimeUnit.HOURS);
            return true;
        }

        log.info("token为空或token过期未登录");
        //如果token未空 说明请求头里面没token 也就是没登录  直接俄重定向到首页
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


    public boolean isCheck(String[] urls,String url){
        for (int i=0;i<urls.length;i++){
            if (ANT_PATH_MATCHER.match(urls[i],url)){
                return true;
            }
        }
        return false;
    }
}
