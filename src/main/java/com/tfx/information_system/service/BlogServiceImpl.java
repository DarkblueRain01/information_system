package com.tfx.information_system.service;


import com.tfx.information_system.NotFoundException;
import com.tfx.information_system.dao.BlogRepository;
import com.tfx.information_system.po.Blog;
import com.tfx.information_system.po.Type;
import com.tfx.information_system.po.User;
import com.tfx.information_system.util.MarkdownUtils;
import com.tfx.information_system.util.MyBeanUtils;
import com.tfx.information_system.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service("BlogService")
public class BlogServiceImpl implements BlogService{
    @Autowired
    private BlogRepository blogRepository;


    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getById(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog=blogRepository.getById(id);
        if(blog==null){
            throw new NotFoundException("该文章不存在");
        }
        blog.setViews(blog.getViews()+1);
        blogRepository.save(blog);

        Blog b=new Blog();
        BeanUtils.copyProperties(blog,b);
        String content=b.getContent();

        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            //条件的动态组合就这下面的toPredicate函数中处理

            /**
             *
             * @param root 可以将表中的字段映射到root对象
             * @param cq 是一个查询的条件容器
             * @param cb 设置具体某一个条件的表达式
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //组合的条件放在List里面
                List<Predicate> predicates=new ArrayList<>();
                if("".equals(blog.getTitle())&&blog.getTitle()!=null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId()!=null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog, User user) {
        return blogRepository.findBlogByUserEquals(user,pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable ) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for(String year:years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {

        if(blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);//设置浏览次数为0
        }else {
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b=blogRepository.getById(id);
        if(b==null){
            throw new NotFoundException("该文章不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
