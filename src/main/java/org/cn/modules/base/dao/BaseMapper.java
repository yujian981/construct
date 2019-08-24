package org.cn.modules.base.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Cty
 */
@Repository
public interface BaseMapper {

    // 批量删除用户
    Integer delUserByids(@Param("list") String[] list);
}
