package xin.l024.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xin.l024.blog.entity.Blog;
import xin.l024.blog.entity.BlogType;
import xin.l024.blog.entity.Comment;
import xin.l024.blog.entity.User;

import java.util.List;

public interface CommentService {
    //保存评论
    public Blog saveComment(long uid, long bid, String contont);

    //根据评论内容分页查询
    public Page<Comment> findByContentLike(String content, Pageable pageable);

    //查询一共有多少条数据
    public Long getCountByContent(String content);

    //根据id删除评论
    public boolean delComment(Long id);
}
