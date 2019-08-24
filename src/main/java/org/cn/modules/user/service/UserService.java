package org.cn.modules.user.service;

import org.cn.common.utils.ServerResponse;
import org.cn.modules.user.entity.User;


public interface UserService {

    //查询所有
    ServerResponse getUserList();

    //新增
    ServerResponse saveUser(User user);

    //删除
    ServerResponse deleteUserById(String id);

    //修改
    ServerResponse updateUserById(User user);
}
