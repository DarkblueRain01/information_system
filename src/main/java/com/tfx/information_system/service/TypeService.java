package com.tfx.information_system.service;


import com.tfx.information_system.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    //保存类型
    Type saveType(Type type);

    //根据Id获取类型
    Type getType(Long id);

    //根据名称获取类型
    Type getTypeByName(String name);

    //分页查询类型
    Page<Type> listType(Pageable pageable);

    //查询所有类型
    List<Type> listType();

    //定义拿到数据的多少
    List<Type> listTypeTop(Integer size);

    //更新类型
    Type updateType(Long id, Type type);

    //删除类型
    void deleteType(Long id);
}
