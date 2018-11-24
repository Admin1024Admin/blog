package xin.l024.blog.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xin.l024.blog.entity.Blog;
import xin.l024.blog.entity.Comment;
import xin.l024.blog.entity.User;
import xin.l024.blog.repository.BlogRepository;
import xin.l024.blog.repository.CommentRepository;
import xin.l024.blog.repository.UserRepositiry;
import xin.l024.blog.service.CommentService;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepositiry userRepositiry;
    @Override
    public Blog saveComment(long uid, long bid, String contont) {
        User user = userRepositiry.findById(uid).get();
        Blog blog = blogRepository.findById(bid).get();
        Comment com = new Comment(user,contont);
        Comment comment = commentRepository.save(com);
        List<Comment> comments = blog.getComments();
        comments.add(comment);
        blog.setComments(comments);
        Blog b = blogRepository.save(blog);
        return b;
    }

    //根据评论内容分页查询
    @Override
    public Page<Comment> findByContentLike(String content, Pageable pageable) {
        content = "%"+content+"%";
        return commentRepository.findByContentLike(content,pageable);
    }

    //查询一共有多少条数据
    @Override
    public Long getCountByContent(String content) {
        content = "%"+content+"%";
        return commentRepository.getCountByContentLike(content);
    }

    //根据id删除评论
    @Override
    public boolean delComment(Long id) {
        Comment comment = commentRepository.findById(id).get();
        try{
            commentRepository.delete(comment);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
