package xin.l024.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xin.l024.blog.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 新增编辑保存用户
     */
    User saveOrUpdataUser(User user);

    /**
     * 注册用户
     */
    User registerUser(User user);

    /**
     * 删除用户
     */
    void removeUser(Long id);

    /**
     * 根据Id查询用户
     */
    User getUserById(Long id);

    /**
     * 根据用户名进行分页模糊查询
     */
    Page<User> listUsersByUsernameLike(String username, Pageable pageable);

    /**
     * 根据用户名精确查询
     */
    public User findByUsername(String username);
    /**
     * 删除用户
     */
    public boolean delUser(Long id);

    /**
     * 获取热门用户头像
     */
    public List<String[]> getAvatar();

    //获取总条数
    public Long getCount(String username);

    //根据邮箱获取用户
    public User checkEmail(String email);

    //根据网名查询
    public User checkName(String name);
}
