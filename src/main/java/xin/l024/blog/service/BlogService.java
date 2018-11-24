package xin.l024.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xin.l024.blog.entity.Blog;
import xin.l024.blog.entity.User;

import java.util.List;
import java.util.Map;

public interface BlogService {
    //分页查询
    public  List<Blog> blogPage(int page, int limit);
    //按照id查询
    public Blog findById(long id);

    //统计博客篇数
    public long getBlogCount(long id);

    //查询所有博客篇数
    public Long getCount();

    //查询所有博客篇数
    public Long getCountByName(String blogName);

    /**
     * 根据标题进行分页模糊查询
     */
    Page<Blog> findByTitleLike(String title, Pageable pageable);

    //添加博客
    public boolean addBlog(Blog blog,Long typeId,String username);

    //分页查询用户的所有博客
    public List<Blog> getUserBlogPage(long uid,int page,int limit);

    //获取热门文章 根据点赞数量排序
    public List<Blog> getFireBlog(int page, int limit);

    //点赞
    public int dianZan(Long bid);

    //删除
    public void delBlog(Long id);
}
