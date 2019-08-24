package org.cn.modules.base.controller;

import org.cn.common.utils.ServerResponse;
import org.cn.modules.base.entity.User;
import org.cn.modules.base.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Cty
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    //查询所有
    @GetMapping
    public ServerResponse getUserList(){
        return userService.getUserList();
    }

    //新增
    @PostMapping
    public ServerResponse saveUser(User user){
        return userService.saveUser(user);
    }

    //删除
    @DeleteMapping
    public ServerResponse deleteUserById(@RequestParam("id")String id){
        return userService.deleteUserById(id);
    }

    //修改
    @PutMapping
    public ServerResponse updateUserById(User user){
        return userService.updateUserById(user);
    }

    //
    @PostMapping("/delList")
    public ServerResponse delUserByIds(@RequestParam("list[]")String... list){
    return userService.delUserByids(list);
    }
}
