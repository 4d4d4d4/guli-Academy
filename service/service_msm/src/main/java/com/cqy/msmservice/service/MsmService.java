package com.cqy.msmservice.service;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 邮件业务类
 * @author qzz
 */
@Service
public interface MsmService {

    /**
     * 检测邮件信息类
     * @param to
     * @param subject
     * @param text
     */
    void checkMail(String to,String subject,String text);

    /**
     * 发送纯文本邮件
     * @param to
     */
    void sendTextMailMessage(String to) throws IOException, TemplateException;
}