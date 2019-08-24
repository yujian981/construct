package org.cn.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cn.modules.user.entity.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<User> selectList();

}
