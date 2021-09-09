package com.tfx.information_system.service;


import com.tfx.information_system.dao.CommentRepository;
import com.tfx.information_system.po.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service("CommentService")
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentsByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        return commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId!=-1){
            comment.setParentComment(commentRepository.getById(parentCommentId));
        }else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
}
