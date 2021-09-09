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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    private static final String REDIRECT_LIST="redirect:/";

    @GetMapping("/firstPage")
    public String firstPage(){

        return "firstPage";
    }

    @GetMapping("/")
    public String index(@PageableDefault(size=5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         Model model) {
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size=3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model){
        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);
        System.out.println(query);
        return "search";
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }


    @GetMapping("/blog-input")
    public String toCreate(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());

        return "blogs-input-rece";
    }

    @GetMapping("/blogList")
    public String toList(@PageableDefault(size=3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model, HttpSession session){
        User u = (User)session.getAttribute("user");
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog,u));
        return "personBlogs";
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if(blog.getId()==null){
            b=blogService.saveBlog(blog);
        }else{
            b=blogService.updateBlog(blog.getId(),blog);
        }
        if(b==null){
            attributes.addFlashAttribute("message","操作失败");
        }else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/footer/newBlog")
    public String newBlogs(Model model){
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: newBlogList";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }
}
