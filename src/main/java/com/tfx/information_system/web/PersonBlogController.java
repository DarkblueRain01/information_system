package com.tfx.information_system.web;

import com.tfx.information_system.po.Blog;
import com.tfx.information_system.po.User;
import com.tfx.information_system.service.BlogService;
import com.tfx.information_system.service.TagService;
import com.tfx.information_system.service.TypeService;
import com.tfx.information_system.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class PersonBlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size=3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model, HttpSession session){
        User u = (User)session.getAttribute("user");
        model.addAttribute("page",blogService.listBlog(pageable,blog,u));
        //返回personBlogs页面下面的blogList片段，定义片段来实现局部渲染
        return "personBlogs :: blogList";
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog= blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return "blogs-input-rece";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("messagee","删除成功");
        return "redirect:/personBlogs";
    }
}
