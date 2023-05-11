package com.rosevvi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosevvi.config.BaseContext;
import com.rosevvi.domain.User;
import com.rosevvi.service.UserService;
import com.rosevvi.tools.*;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author: rosevvi
 * @date: 2023/3/22 14:26
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 登录
     *
     * @param request
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(HttpServletRequest request, @RequestBody User user) {
        try {
            if ((redisTemplate.opsForValue().get(user.getCode().toLowerCase()) == null)) {
                return Result.error(Code.ERR, "验证码错误或验证码过期");
            }
        } catch (Exception e) {
            return Result.error(Code.ERR,"验证码过期");
        }
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());

        User one = userService.getOne(queryWrapper);
        if (one == null) {
            return Result.error(Code.ERR, "登录失败");
        }
        if (!one.getPassword().equals(password)) {
            return Result.error(Code.ERR, "密码错误");
        }
        if (one.getStatus() == 1) {
            return Result.error(Code.ERR_P, "用户已禁用");
        }
        JsonWebToken jsonWebToken = new JsonWebToken();
        String webTokenJwt = jsonWebToken.getJwt(one.getId());
        //把token放入redis缓存
        redisTemplate.opsForValue().set(one.getId().toString(), webTokenJwt, 5, TimeUnit.DAYS);
        user.setToken(webTokenJwt);
        user.setPassword("");
        log.info(jsonWebToken.getCalim(webTokenJwt) + "wwwwwwwww");
        return Result.success(Code.OK, "登录成功", user);
    }

    /**
     * 验证码
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/verifyCode")
    public Result<String> verifyCode() throws Exception {
        // 三个参数分别为宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(150, 40, 4);
        captcha.setFont(Captcha.FONT_2);
        // 验证码存入redis 设置存在时间为1分钟
        redisTemplate.opsForValue().set(captcha.text().toLowerCase(), 1, 1, TimeUnit.MINUTES);
        captcha.setCharType(Captcha.TYPE_ONLY_LOWER);
//        captcha.out(response.getOutputStream());
        // 输出图片流
        return Result.success(Code.OK, "", captcha.toBase64());
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        User one = userService.getOne(lambdaQueryWrapper);
        if (one != null) {
            return Result.error(Code.ERR, "用户已存在");
        }
        String digest = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(digest);
        Date date = new Date();
        user.setName("睿智的小草" + date.getTime());
        userService.save(user);
        return Result.success(Code.OK, "注册成功");
    }

    /**
     * 短信服务
     *
     * @param phone
     */
    @PostMapping("/SMS/{phone}")
    public void smsService(@PathVariable("phone") String phone) {
        SmsUtils smsUtils = new SmsUtils();
        smsUtils.getSms(phone);
    }

    /**
     * 测试
     *
     * @param user
     * @return
     */
    @PostMapping("/test")
    public Result<String> test(@RequestBody User user) {
        JsonWebToken jsonWebToken = new JsonWebToken();
        jsonWebToken.getCalim(user.getToken());
        return Result.success(Code.OK, "测试成功");
    }

    /**
     * 获取当前用户信息
     *
     * @return
     */
    @GetMapping()
    public Result<User> user() {
        Long userId = BaseContext.getThreadLocal();
        User user = userService.getById(userId);
        user.setPassword(null);
        return Result.success(Code.OK, "查询成功", user);
    }

    /**
     * 登出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logOut() {
        //把token从缓存中清除
        if (null != redisTemplate.opsForValue().get(BaseContext.getThreadLocal().toString())) {
            TokenList.tokens.add((String) redisTemplate.opsForValue().get(BaseContext.getThreadLocal().toString()));
            redisTemplate.delete(BaseContext.getThreadLocal().toString());
            return Result.success(Code.OK, "登出成功");
        }
        return Result.error(Code.ERR, "登出失败");
    }

    /**
     * 修改密码
     *
     * @param password
     * @return
     */
    @PostMapping("/updatePassword")
    public Result<String> updatePassword(@RequestParam String password) {
        User user = userService.getById(BaseContext.getThreadLocal());
        String passwordMD5 = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(passwordMD5);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId, user.getId());
        userService.update(user, userLambdaQueryWrapper);
        return Result.success(Code.OK, "更新成功");
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @PostMapping("/updateUser")
    public Result<String> updateUser(@RequestBody User user) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId, BaseContext.getThreadLocal());
        userService.update(user, userLambdaQueryWrapper);
        return Result.success(Code.OK, "更新成功");
    }
}
