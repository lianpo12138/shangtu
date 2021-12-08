package com.dubu.turnover.core;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 基于通用MyBatis Mapper插件的Service接口
 */
public abstract class AbstractService<T> {

    @Autowired
    protected Mapper<T> mapper;

    private Class<T> modelClass;    // 当前泛型真实类型的Class

    public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public void save(T model) {
        mapper.insertSelective(model);
    }

    public void save(List<T> models) {
        mapper.insertList(models);
    }

    public void deleteById(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }

    public void updateById(T model) {
        mapper.updateByPrimaryKeySelective(model);
    }

    public T selectById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    public T selectOne(String fieldName, Object value) throws TooManyResultsException {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    public List<T> selectByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    public List<T> selectByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }
    
    public Integer selectCountByCondition(Condition condition) {
        return mapper.selectCountByCondition(condition);
    }

    public List<T> selectAll() {
        return mapper.selectAll();
    }

    public PageInfo<T> selectPage(Integer page, Integer pageSize, Condition condition) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(mapper.selectByCondition(condition));
    }
}
