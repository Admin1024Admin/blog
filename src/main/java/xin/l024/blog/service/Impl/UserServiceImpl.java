package xin.l024.blog.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xin.l024.blog.entity.User;
import xin.l024.blog.repository.UserRepositiry;
import xin.l024.blog.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService,UserDetailsService {
    @Autowired
    private UserRepositiry userRepositiry;
    @Transactional
    @Override
    public User saveOrUpdataUser(User user) {
        return userRepositiry.save(user);
    }
    @Transactional
    @Override
    public User registerUser(User user) {
        return userRepositiry.save(user);
    }

    @Transactional
    @Override
    public void removeUser(Long id) {
        userRepositiry.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepositiry.findById(id).get();
    }

    @Override
    public Page<User> listUsersByUsernameLike(String name, Pageable pageable) {
        name = "%"+name+"%";
        return userRepositiry.findByUsernameLike(name,pageable);
    }

    @Override
    public User findByUsername(String username) {
        return userRepositiry.findByUsername(username);
    }

    @Override
    public boolean delUser(Long id) {
         try{
             userRepositiry.deleteById(id);
             return true;
         }catch(Exception e){
             e.printStackTrace();
             return false;
         }
    }

    /**
     *  获取热门用户的头像
     * @return
     */
    @Override
    public List<String[]> getAvatar() {
        List<String[]> avatars = userRepositiry.getUserOrderByLikeSizeLimit10();
        return avatars;
    }

    @Override
    public Long getCount(String username) {
        return userRepositiry.getCount(username);
    }

    @Override
    public User checkEmail(String email) {
        return userRepositiry.findByEmail(email);
    }

    @Override
    public User checkName(String name) {
        return userRepositiry.findByName(name);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("名字是"+s);
        return userRepositiry.findByUsername(s);
    }
}
