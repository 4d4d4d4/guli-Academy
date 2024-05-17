package com.cqy.msmservice.service.impl;
import com.cqy.msmservice.service.MsmService;
import com.cqy.servicebase.exceptionhandler.GuliException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.RandomUtil;
@Service
public class MsmServiceImpl implements MsmService {
    private static final Logger logger = LoggerFactory.getLogger(MsmService.class);

    /**
     * 注入邮件工具类
     */
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String sendMailer;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * thymeleaf html 渲染对象
     */
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 检测邮件信息类
     * @param to
     * @param subject
     * @param text
     */

    public void checkMail(String to,String subject,String text){
        if(StringUtils.isEmpty(to)){
            throw new RuntimeException("邮件收信人不能为空");
        }
        if(StringUtils.isEmpty(subject)){
            throw new RuntimeException("邮件主题不能为空");
        }
        if(StringUtils.isEmpty(text)){
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    /**
     * 发送纯文本邮件
     * @param to
     */
    public void sendTextMailMessage(String to) throws IOException, TemplateException {

        try {

            //true 代表支持复杂的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(),true);
            //邮件发信人
            mimeMessageHelper.setFrom(sendMailer);
            //邮件收信人  1或多个
            mimeMessageHelper.setTo(to.split(","));
            //邮件主题
            mimeMessageHelper.setSubject("自研学苑");
            //邮件内容
//            mimeMessageHelper.setText(text);
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());

            //构建 Freemarker 的基本配置
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);

            // 配置模板位置
            ClassLoader classLoader = this.getClass().getClassLoader();

            // 配置模板位置 -->  template包下
            configuration.setClassLoaderForTemplateLoading(classLoader,"templates");

            //加载模板 （入参具体模板名称）
            Template template = configuration.getTemplate("freemarker.html");

            //模板参数
            Map<String,Object> map = new HashMap();//可以自定义自己的对象
            String code = getyzm();
            map.put("code",code);

            StringWriter out = new StringWriter();
            //模板渲染，渲染结果存放到out中 将out中的html字符串发送
            template.process(map,out);

            //邮件内容（html渲染 要用ture）
            mimeMessageHelper.setText(out.toString(),true);

            //内嵌照片
            FileSystemResource fileSystemResource = new FileSystemResource(new File("E:\\壁纸\\goukong.jpg"));

            //内嵌的名字 ‘aa’ 要和 html 中img标签里的保持一样
            mimeMessageHelper.addInline("aa",fileSystemResource);

            try {
                //发送邮件
                javaMailSender.send(mimeMessageHelper.getMimeMessage());
            }catch (Exception e){
                e.printStackTrace();
                throw new GuliException(20001,"邮箱有误！请检查");
            }
            //存入redis中 并且设置了5分钟的有效时间哦
            redisTemplate.opsForValue().set(to,code,5, TimeUnit.MINUTES);
            System.out.println("发送邮件成功："+sendMailer+"->"+to);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new GuliException(20001,"验证码发送失败");
        }
    }

    /**
     * 生成四位验证码
     * @return
     */
    public static String getyzm(){
        char[] arrays = new char[4];
        for (int i = 0; i < 4; i++) {
            int num = (int)Math.floor(Math.random()*58)+65;
            if (num>90&&num<97) {
                num=(int)Math.floor(Math.random()*10);
                arrays[i]=Integer.toString(num).charAt(0);
                continue;
            }
            arrays[i]=(char)num;
        }
        return new String(arrays);
    }
}
