package com.tfx.information_system.web;


import com.tfx.information_system.po.Comment;
import com.tfx.information_system.po.User;
import com.tfx.information_system.service.BlogService;
import com.tfx.information_system.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{id}")
    public String comments(@PathVariable Long id, Model model){
        model.addAttribute("comments",commentService.listCommentsByBlogId(id));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        if(user!=null){
            comment.setAvatar(user.getAvatar());
            User publisher= comment.getBlog().getUser();
            if(publisher.getNickname()==comment.getNickname()){
                comment.setAdminComment(true);
            }else{
                comment.setAdminComment(false);
            }
            comment.setNickname(user.getNickname());
        }else{
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/"+blogId;
    }

    /**
     * 循环每个顶级的评论节点
     */
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView =new ArrayList<>();
        for(Comment comment:comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments){
        for(Comment comment:comments){
            List<Comment> replies1 = comment.getReplyComments();
            for(Comment reply1 : replies1){
                // 循环迭代，找出子代，存放在tempReplies中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplies);
            //清除临时存放区
            tempReplies = new ArrayList<>();
        }
    }


    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplies = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     */
    private void recursively(Comment comment){
        tempReplies.add(comment);//顶节点添加到临时存放集合
        if(comment.getReplyComments().size()>0){
            List<Comment> replies = comment.getReplyComments();
            for(Comment reply : replies){
                tempReplies.add(reply);
                if(reply.getReplyComments().size()>0){
                    recursively(reply);
                }
            }
        }
    }
}
