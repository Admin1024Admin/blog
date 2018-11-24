package xin.l024.blog.controller;

import net.sf.json.JSONObject;
import org.apache.http.protocol.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xin.l024.blog.dto.Menu;
import xin.l024.blog.entity.Blog;
import xin.l024.blog.entity.Comment;
import xin.l024.blog.entity.Setting;
import xin.l024.blog.entity.User;
import xin.l024.blog.service.BlogService;
import xin.l024.blog.service.CommentService;
import xin.l024.blog.service.SettingService;
import xin.l024.blog.service.UserService;
import xin.l024.blog.util.JsonConfigUtil;
import xin.l024.blog.util.Menus;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admins")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/login")
    public String login(){
        return "admin/login";
    }
    @PostMapping("/index")
    public String index(User user){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = userDetails.getUsername();
        String pass = userDetails.getPassword();
        System.out.println("name---->"+name+"---->pass--->"+pass);
        User u = userService.findByUsername(user.getUsername());
        if(u!=null){
            if(u.getPassword().equals(pass)&&u.getUsername().equals(name)){
                return "admin/admin";
            }
        }
        return "admin/login";
    }
    /**
     * 用户管理---->分页
     */
    @GetMapping("/user")
    @ResponseBody
    public String user(@RequestParam(value = "page",required = false,defaultValue ="1")int page,
                        @RequestParam(value = "limit",required = false,defaultValue = "10")int limit,
                        @RequestParam(value = "username",required = false,defaultValue = "")String username){
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","");
        //放入总条数
        map.put("count",userService.getCount(username));
        Pageable pageable =  PageRequest.of((page-1)*limit,limit);
        Page<User> pageUser = userService.listUsersByUsernameLike(username, pageable);
        List<User> users = pageUser.getContent();
        map.put("data", users);
        JSONObject jsonObject = JSONObject.fromObject(map, JsonConfigUtil.getJsonConfig());
        System.out.println("json-->"+jsonObject);
        return jsonObject.toString();
    }

    /**
     * 博客管理
     */
    @GetMapping("/blogs")
    @ResponseBody
    public String blogs(@RequestParam(value = "page",required = false,defaultValue ="1")int page,
                        @RequestParam(value = "limit",required = false,defaultValue = "10")int limit,
                        @RequestParam(value = "title",required = false,defaultValue = "")String title){
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","");
        //放入总条数
        System.out.println("page--->"+page+"--->limit----->"+limit);
        map.put("count",blogService.getCountByName(title));
        Pageable pageable =  PageRequest.of((page-1)*limit,limit);
        Page<Blog> pageBlog = blogService.findByTitleLike(title, pageable);
        List<Blog> blogs = pageBlog.getContent();
        map.put("data", blogs);
        JSONObject jsonObject = JSONObject.fromObject(map, JsonConfigUtil.getJsonConfig());
        System.out.println("json-->"+jsonObject);
        return jsonObject.toString();
    }

    /**
     * 评论管理
     */
    @GetMapping("/comments")
    @ResponseBody
    public String comments(@RequestParam(value = "page",required = false,defaultValue ="1")int page,
                        @RequestParam(value = "limit",required = false,defaultValue = "10")int limit,
                        @RequestParam(value = "content",required = false,defaultValue = "")String content){
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","");
        //放入总条数
        System.out.println("page--->"+page+"--->limit----->"+limit);
        map.put("count",commentService.getCountByContent(content));
        Pageable pageable =  PageRequest.of((page-1)*limit,limit);
        Page<Comment> pageComment = commentService.findByContentLike(content, pageable);
        List<Comment> comments = pageComment.getContent();
        map.put("data", comments);
        JSONObject jsonObject = JSONObject.fromObject(map, JsonConfigUtil.getJsonConfig());
        System.out.println("json-->"+jsonObject);
        return jsonObject.toString();
    }
    /**
     * 设置网站名称
     */
    @PostMapping("/setting")
    @ResponseBody
    public String updateNetName(HttpSession session, Setting setting){
        setting.setId(1);
        Setting set = settingService.updataSetting(setting);
        if(set!=null){
            session.setAttribute("netName",set.getTitle());
            return "success";
        }
        return "error";
    }

}
