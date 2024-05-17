package com.cqy.msmservice.controller;

import com.cqy.commonutils.Result;
import com.cqy.msmservice.service.MsmService;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 发送文本邮件
     * @param to 邮件收信人
     */
    @PostMapping("/sendTextMail/{email}")
    public Result sendTextMail(@PathVariable("email") String to) throws TemplateException, IOException {
        //现在redis中获取验证码 有则直接返回
        String isCode = (String) redisTemplate.opsForValue().get(to);
        System.out.println("'''''''''''''"+isCode);
        if(!StringUtils.isEmpty(isCode)){
            return  Result.ok();
        }


        msmService.sendTextMailMessage(to);
        return Result.ok().message("验证码发送成功");
    }
}
