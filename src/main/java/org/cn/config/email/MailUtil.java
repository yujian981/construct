package org.cn.config.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;


/**
 * @Author Cty
 * @Description //TODO 邮件工具类
 **/
public class MailUtil {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    @Value("${cus.email}")
    private String from;

    public void sendMails(){
        System.out.println("Git 更新测试");
    }

    public void senMailUtil(){
        System.out.println(" ceshi ");
    }

}
