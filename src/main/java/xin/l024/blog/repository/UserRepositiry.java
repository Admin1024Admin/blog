package xin.l024.blog.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xin.l024.blog.entity.User;

import java.util.List;

public interface UserRepositiry extends JpaRepository<User,Long> {
    /**
     * 根据用户名分页查询用户列表
     */
    Page<User> findByUsernameLike(String username,Pageable pageable);

    /**
     * 根据用户账号查询用户
     */
    User findByUsername(String username);

    /**
     * 查询热门用户 根据点赞次数最多的排序查询出10个
     */
    @Query(value = "select avatar,username from user where id in(select user_id from blog GROUP BY user_id) limit 9",nativeQuery = true)
    List<String[]> getUserOrderByLikeSizeLimit10();

    //查询一共有多少条数据
    @Query(value = "select count(id) from user where username like %?1%",nativeQuery = true)
    Long getCount(String username);

    //根据邮箱查询用户
    public User findByEmail(String email);

    //根据网名查
    public User findByName(String name);
}
