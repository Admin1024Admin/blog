package xin.l024.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xin.l024.blog.entity.BlogType;

import java.util.List;

public interface BlogTypeRepository extends JpaRepository<BlogType,Long> {
    /**
     * 根据用户id查询出所有的博分类
     */
    @Query(value = "select * from blog_type where user_id=?1",nativeQuery = true)
    public List<BlogType> findByUserId(long uid);

}
