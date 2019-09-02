package org.cn.base;
import java.io.Serializable;


@FunctionalInterface
public interface BaseService<E, ID extends Serializable>  {

    public BaseDao<E,ID> getRepository();

    public default E get(ID id) { return getRepository().getOne(id); }

}
