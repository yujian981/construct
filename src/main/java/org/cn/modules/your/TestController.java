package org.cn.modules.your;

import lombok.Data;
import org.cn.common.utils.ExcelUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Cty
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/login")
@Transactional
public class TestController {


    @GetMapping
    public String test1(){
        return "login登录！";
    }

    @GetMapping("/test1")
    public void test(HttpServletResponse response){

        List<Map<String,Object>> maps=new ArrayList<>();
        List<String> title=new ArrayList<>();
        title.add("序号");
        title.add("名称");
        title.add("年龄");
        title.add("入会时间");
        for (int i = 0; i <10 ; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("序号","111");
            map.put("名称","ceshi");
            map.put("年龄",12);
            map.put("入会时间",new Date());
            maps.add(map);
        }

        ExcelUtil.sendExcel(response,"xsl","测试一下啊",title,maps);

        //return "域名成功！";
    }

    @Data
    private class User{
        private String id;
        private String name;
        private String age;
        private Date addTime;
    }


    @PostMapping("/test")
    public String test2() {
        System.out.println("二级域名");
        return "二级域名成功！";
    }

}


