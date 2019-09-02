package org.cn.base;


import io.swagger.annotations.ApiOperation;
import org.cn.common.constant.Result;
import org.cn.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

/**
 * @author Cty
 */
public abstract class BaseController<E,ID extends Serializable> {


    @Autowired
    public abstract BaseService<E,ID > getService();

    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "通过id获取")
    public Result<E> get(@PathVariable ID id){
       E entity = getService().get(id);
       return new ResultUtil<E>().setData(entity);
    }

}
