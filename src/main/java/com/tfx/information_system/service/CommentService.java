package com.tfx.information_system.service;



import com.tfx.information_system.po.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentsByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
