package com.tfx.information_system.service;


import com.tfx.information_system.po.Blog;
import com.tfx.information_system.po.User;
import com.tfx.information_system.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    //根据ID获取博客
    Blog getBlog(Long id);

    //前端界面博客内容转换成html
    Blog getAndConvert(Long id);

    //分页查询
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
    //用户个人内容查询
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog, User user);
    //前台展示分页查询
    Page<Blog> listBlog(Pageable pageable);

    //根据标签id，进行分页查询
    Page<Blog> listBlog(Long tagId, Pageable pageable);

    //根据输入的字符串进行查询
    Page<Blog> listBlog(String query, Pageable pageable);

    //size拿到数据的多少，推荐博客列表
    List<Blog> listRecommendBlogTop(Integer size);

    //根据年份对博客进行展示
    Map<String,List<Blog>> archiveBlog();

    //对现有博客计数
    Long countBlog();

    //新增博客
    Blog saveBlog(Blog blog);

    //修改博客
    Blog updateBlog(Long id, Blog blog);

    //删除博客
    void deleteBlog(Long id);
}
