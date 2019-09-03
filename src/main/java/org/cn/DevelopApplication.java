package org.cn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @Author Cty
 **/
// Activiti5.22需要排除 SecurityAutoConfiguration.class
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class})
@EnableTransactionManagement
@MapperScan("org.cn.modules.*.dao")
public class DevelopApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevelopApplication.class, args);
    }

}
