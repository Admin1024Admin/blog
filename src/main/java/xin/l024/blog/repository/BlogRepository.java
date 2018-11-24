package xin.l024.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import xin.l024.blog.entity.Blog;
import xin.l024.blog.entity.User;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long> {
////    List<Blog> findByOrderByCreateTimeDesc(Pageable pageable);
        //分页
        @Query(value = "select * from blog order by create_time desc limit ?1,?2",nativeQuery = true)
       public List<Blog> getPage(int page,int limit);

        //统计用户所有的博客数量
        @Query(value = "select count(*) from blog where user_id=?1",nativeQuery = true)
        public Long getBlogCount(long userId);

        //统计用户所有的博客数量
        @Query(value = "select count(*) from blog where title like ?1",nativeQuery = true)
        public Long getBlogCountByName(String blogName);

        //分页查询用户的博客
        @Query(value = "select * from blog where user_id=?1 order by create_time desc limit ?2,?3",nativeQuery = true)
        public List<Blog> getBlogPageByUid(long uid,int page,int limit);

        /**
         * 根据用户名分页查询用户列表
         */
        Page<Blog> findByTitleLike(String title, Pageable pageable);

        //根据点赞数最多的用户的博客
        @Query(value = "select * from blog order by like_size desc limit ?1,?2",nativeQuery = true)
        public List<Blog> getBlogByLikeSize(int page, int limit);

        //查看。每次查看+1
       @Modifying
       @Query(value = "update blog set read_size = read_size+1 where id = ?1",nativeQuery = true)
        public int setView(long bid);

       //点赞
       @Modifying
       @Query(value = "update blog set like_size = like_size+1 where id = ?1",nativeQuery = true)
       public int setLike(long bid);

       //根据标题搜索
       Page<Blog> findBlogByTitleLike(String title, Pageable pageable);
}
