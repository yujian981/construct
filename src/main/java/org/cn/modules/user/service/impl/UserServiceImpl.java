package org.cn.modules.user.service.impl;

import org.cn.common.utils.ServerResponse;
import org.cn.modules.user.dao.UserMapper;
import org.cn.modules.user.entity.User;
import org.cn.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Cty
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * @Author Cty
     * @Description //TODO mybatis 查询
     **/
    @Override
    public ServerResponse getUserList() {

      List<User> user =userMapper.selectList();

        return ServerResponse.success(user);
    }

    /**
     * @Author Cty
     * @Description //TODO mybatis 新增
     **/
    @Override
    public ServerResponse saveUser(User user) {

        int findI = userMapper.insert(user);

        return ServerResponse.success(findI);
    }

    /**
     * @Author Cty
     * @Description //TODO mybatis 删除
     **/
    @Override
    public ServerResponse deleteUserById(String id) {

        int i = userMapper.deleteById(id);

        return ServerResponse.success(i);
    }

    /**
     * @Author Cty
     * @Description //TODO mybatis  修改
     **/
    @Override
    public ServerResponse updateUserById(User user) {

        int i = userMapper.updateById(user);

        return ServerResponse.success(i);
    }
}
