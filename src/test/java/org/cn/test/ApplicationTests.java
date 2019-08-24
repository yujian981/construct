package org.cn.test;

import org.cn.common.utils.RandomUtil;
import org.cn.config.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author Cty
 * @Description //TODO 测试类
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    //@Autowired 引用方法类
    @Autowired
    private JavaMailSender mailSender;
    //自定义注解调用
    @Value("${cus.email}")
    private String from;


    @Test
    public void redisTest() {
        for (int i = 9999; i > 0; i--) {
            RedisUtil.set(RandomUtil.UUID32(), "测试");
        }
    }


    @Test
    public void emailTest() {

        SimpleMailMessage message = new SimpleMailMessage();
        String[] attr = {"13937966441@163.com", "15116952620@163.com"};
        message.setFrom(from);
        message.setTo(attr);
        message.setSubject("早上包子好吃11");
        message.setText("测试一下");

        System.out.println("hello word!111");
        mailSender.send(message);
    }


}
