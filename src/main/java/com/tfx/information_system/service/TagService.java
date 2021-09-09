package com.tfx.information_system.service;


import com.tfx.information_system.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    //保存标签
    Tag saveTag(Tag tag);

    //根据Id获取标签
    Tag getTag(Long id);

    //根据名称获取标签
    Tag getTagByName(String name);

    //分页查询标签
    Page<Tag> listTag(Pageable pageable);

    //查询所有标签
    List<Tag> listTag();

    //根据字符串参数查询
    List<Tag> listTag(String ids);

    //定义拿到数据的多少
    List<Tag> listTagTop(Integer size);

    //更新修改标签
    Tag updateTag(Long id, Tag tag);

    //删除标签
    void deleteTag(Long id);
}
