package xin.l024.blog.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xin.l024.blog.entity.Blog;
import xin.l024.blog.entity.BlogType;
import xin.l024.blog.entity.User;
import xin.l024.blog.repository.BlogRepository;
import xin.l024.blog.repository.BlogTypeRepository;
import xin.l024.blog.repository.UserRepositiry;
import xin.l024.blog.service.BlogService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepositiry userRepositiry;
    @Autowired
    private BlogTypeRepository blogTypeRepository;
    //查看开关
    private long viewBlogId;
    /**
     * 分页查询
     * @param page
     * @param limit
     * @return
     */
    @Override
    public List<Blog> blogPage(int page, int limit) {
        page = (page-1)*limit;
        //防止恶意请求更改页码
        if(page<0){
            return blogRepository.getPage(0,4);
        }
        return blogRepository.getPage(page,limit);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Blog findById(long id) {
        if(viewBlogId!=id){
            //每次查看更新read_size
            blogRepository.setView(id);
        }
        viewBlogId=id;
        return blogRepository.findById(id).get();
    }

    /**
     * 根据用户id统计发表的博客篇数
     * @param id
     * @return
     */
    @Override
    public long getBlogCount(long id) {
//        Blog blog = new Blog();
//        User user = new User();
//        user.setId(id);
//        blog.setUser(user);
//        Example<Blog> userExample = Example.of(blog);
        return blogRepository.getBlogCount(id);
    }

    /**
     * 获取所有的博客篇数
     * @return
     */
    @Override
    public Long getCount() {
        return blogRepository.count();
    }

    @Override
    public Long getCountByName(String blogName) {
        blogName = "%"+blogName+"%";
        return blogRepository.getBlogCountByName(blogName);
    }

    @Override
    public Page<Blog> findByTitleLike(String title, Pageable pageable) {
        title = "%"+title+"%";
        return blogRepository.findByTitleLike(title,pageable);
    }


    @Override
    public boolean addBlog(Blog blog, Long typeId, String username) {
        //根据username查询到用户
        User user = userRepositiry.findByUsername(username);
        blog.setUser(user);
        //保存博客，与用户绑定
        Blog saveBlog = blogRepository.save(blog);
        //根据typeId查询到分类
        BlogType blogType = blogTypeRepository.findById(typeId).get();
        List<Blog> blogs = new ArrayList<>();
        //将分类和博客绑定
        blogs.add(saveBlog);
        blogType.setBlogs(blogs);
        //保存
        BlogType saveblogType = blogTypeRepository.save(blogType);
        if(saveBlog!=null&&saveblogType!=null){
            return true;
        }
        return false;
    }

    /**
     * 根据用户id查询所有的博客
     */
    @Override
    public List<Blog> getUserBlogPage(long uid, int page, int limit) {
        page = (page-1)*limit;
        //防止前端恶意请求更改页码
        if(page<0){
            return blogRepository.getBlogPageByUid(uid,0,4);
        }
        return blogRepository.getBlogPageByUid(uid,page,limit);
    }

    @Override
    public List<Blog> getFireBlog(int page, int limit) {
        page = (page-1)*limit;
        return blogRepository.getBlogByLikeSize(page,limit);
    }

    @Transactional
    @Override
    public int dianZan(Long bid) {
        return blogRepository.setLike(bid);
    }

    @Override
    public void delBlog(Long id) {
         blogRepository.deleteById(id);
    }

}
