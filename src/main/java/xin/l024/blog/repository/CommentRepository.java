package xin.l024.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xin.l024.blog.entity.Comment;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment,Long>{
    @Query(value = "select * from comment where user_id=?1",nativeQuery = true)
    public List<Comment> getComents(Long userId);

    //根据评论内容分页查询
    public Page<Comment> findByContentLike(String content, Pageable pageable);

    @Query(value = "select count(id) from comment where content like ?1",nativeQuery = true)
    public Long getCountByContentLike(String content);
}
