package xin.l024.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xin.l024.blog.dto.Response;
import xin.l024.blog.entity.Authority;
import xin.l024.blog.entity.BlogType;
import xin.l024.blog.entity.User;
import xin.l024.blog.service.AuthorityService;
import xin.l024.blog.service.BlogTypeService;
import xin.l024.blog.service.UserService;
import xin.l024.blog.util.Menus;

import javax.management.MalformedObjectNameException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController{
    @Autowired
    private UserService userService;
    @Autowired
    AuthorityService authorityService;
    @Autowired
    private BlogTypeService blogTypeService;
    private static final Long ROLE_USER_AUTHORITY_ID = 2L;
    /**
     * 添加用户
     */
    @PostMapping("/save")
    @ResponseBody
    public Response addUser(User user,@RequestParam("authorityId") String authorityId){
        System.out.println(authorityId+"----id");
        //设置角色
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(Long.valueOf(authorityId)));
        user.setAuthorities(authorities);
        System.out.println("user-->"+user);
        user.setEncodePassword(user.getPassword()); // 加密密码
        User u = userService.saveOrUpdataUser(user);
        if(u!=null){
            Response response = new Response(true,"添加成功","");
            return response;
        }
        Response response = new Response(false,"添加失败","");
        return response;
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Response editUser(User user,@RequestParam(value = "isAdmin",defaultValue = "false")Boolean isAdmin,@RequestParam(value = "authorityId",required = false)Long authorityId){
        System.out.println("修改的user--->"+user);
        User u = userService.getUserById(user.getId());
        if(isAdmin){
            user.setPassword(u.getPassword());
            Authority authority = authorityService.getAuthorityById(authorityId);
            List<Authority> authorities = new ArrayList<>();
            authorities.add(authority);
            user.setAuthorities(authorities);
        }else{
            user.setAuthorities(u.getAuthorities2());
        }
        //判断是否修改了密码
        if(!u.getPassword().equals(user.getPassword())){
            user.setEncodePassword(user.getPassword());
        }
        user.setId(u.getId());
        user.setAvatar(u.getAvatar());
        User u2 = userService.saveOrUpdataUser(user);
        if(u2!=null){
            Response response = new Response(true,"修改成功","");
            return response;
        }
        Response response = new Response(false,"修改失败","");
        return response;
    }
    /**
     *  注册用户
     */
    @PostMapping("/register")
    @ResponseBody
    public Response registerUser(User user){
        //设置角色
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
        user.setAuthorities(authorities);
        System.out.println("user-->"+user);
        //加密密码
        user.setEncodePassword(user.getPassword());
        User u = userService.registerUser(user);
        if(u!=null){
            Response response = new Response(true,"注册成功","");
            return response;
        }
        Response response = new Response(false,"注册失败","");
        return response;
    }

    /**
     * 按照id查询用户
     */
    @GetMapping("/get/{id}")
    @ResponseBody
    public Map<String,Object> getUserById(@PathVariable("id")Long id){
        Map<String,Object> map = new HashMap<>();
        User user = userService.getUserById(id);
        //获取用户角色的id 用来判断修改界面选择框的值
        Long authorityId = user.getAuthorities2().get(0).getId();
        map.put("user",user);
        map.put("authorityId",authorityId);
        return map;
    }

    /**
     * 按照id删除用户
     */
    @GetMapping("/del/{id}")
    @ResponseBody
    public boolean delUser(@PathVariable("id") Long id){
        return userService.delUser(id);
    }
    /**
     * 分页查询
     */
    @GetMapping("/page")
    public String findAllPage(  @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                                @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                                @RequestParam(value="keyword",required=false,defaultValue="") String name,
                                Model model){
        Pageable pageable =  PageRequest.of(pageIndex,pageSize);
        Page<User> page = userService.listUsersByUsernameLike(name, pageable);
        List<User> users = page.getContent();	// 当前所在页面数据列表
        model.addAttribute("page", page);
        model.addAttribute("users", users);
        model.addAttribute("menus", Menus.getMenus());
        return "/admin/index";
    }

    //添加分类
    @PostMapping("/addType")
    @ResponseBody
    public Response addType(@RequestParam("uid")Long uid,@RequestParam("typeName")String typeName){
        System.out.println("uid--->"+uid+"---->typeName---->"+typeName);
        User user = userService.getUserById(uid);
        BlogType blogType = blogTypeService.savaType(user,typeName);
        if(blogType!=null){
            Response response = new Response(true,"添加成功",blogType);
            return response;
        }
        Response response = new Response(false,"添加失败","");
        return response;
    }
}
