package org.cn.modules.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cn.modules.base.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {

    List<User> selectList();

}
