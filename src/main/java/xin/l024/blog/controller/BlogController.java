package xin.l024.blog.controller;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xin.l024.blog.dto.Response;
import xin.l024.blog.entity.Blog;
import xin.l024.blog.entity.BlogType;
import xin.l024.blog.entity.Comment;
import xin.l024.blog.entity.User;
import xin.l024.blog.service.BlogService;
import xin.l024.blog.service.BlogTypeService;
import xin.l024.blog.service.CommentService;
import xin.l024.blog.service.UserService;
import xin.l024.blog.util.DataFormat;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogTypeService blogTypeService;
    @Autowired
    private UserService userService;
    //点赞开关view
    private long likeBlogId;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public String blogPage(@RequestParam(value = "page",required = false,defaultValue ="1")int page,
                        @RequestParam(value = "limit",required = false,defaultValue = "4")int limit,Map<String,Object> map){
        System.out.println("page---------->"+page+"--------limit--------->"+limit);
        List<Blog> blogs = blogService.blogPage(page,limit);
        map.put("blogs",blogs);
        map.put("page",page);
        double pageCount =Math.ceil((double)blogService.getCount()/limit);
        map.put("pageCount",(int)pageCount);
        //将标签放进HashSet 去重复
        HashSet<String> tags = new HashSet<String>();
        for(Blog b:blogs){
            String tag = b.getTags();
            if(tag!=null&&tag.length()>0){
                String[] tagArr = tag.split(",");
                for(String str:tagArr){
                    tags.add(str);
                }
            }
        }
        System.out.println("所有标签--->"+tags);
       map.put("tags",tags);
       //将热门用户头像放入
        List<String[]> avatars = userService.getAvatar();
        System.out.println("热门用户--->"+avatars);
        map.put("avatars",avatars);
        //将热门文章放入
        List<Blog> titles = blogService.getFireBlog(1,5);
        System.out.println("热门文章--->"+titles);
        map.put("titles",titles);
        return "/index2";
    }
    /**
     * 最热查询排序
     */
    @GetMapping("/fire")
    public String fireBlog(@RequestParam(value = "page",required = false,defaultValue ="1")int page,
                           @RequestParam(value = "limit",required = false,defaultValue = "4")int limit,Map<String,Object> map){
        //将热门文章放入
        List<Blog> blogs = blogService.getFireBlog(page,limit);
        map.put("blogs",blogs);
        map.put("page",page);
        double pageCount =Math.ceil((double)blogService.getCount()/limit);
        map.put("pageCount",(int)pageCount);
        //将标签放进HashSet 去重复
        HashSet<String> tags = new HashSet<String>();
        for(Blog b:blogs){
            String tag = b.getTags();
            if(tag!=null&&tag.length()>0){
                String[] tagArr = tag.split(",");
                for(String str:tagArr){
                    tags.add(str);
                }
            }
        }
        System.out.println("所有标签--->"+tags);
        map.put("tags",tags);
        //将热门用户头像放入
        List<String[]> avatars = userService.getAvatar();
        System.out.println("热门用户--->"+avatars);
        map.put("avatars",avatars);
        //将热门文章放入
        List<Blog> titles = blogService.getFireBlog(1,5);
        System.out.println("热门文章--->"+titles);
        map.put("titles",titles);
        return "/blog/fire";
    }
    /**
     * 根据id查询博客内容
     */
    @GetMapping("/{id}")
    public String getBlog(@PathVariable("id")Long id,Map<String,Object>map){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        map.put("user",user);
        Blog blog = blogService.findById(id);
        map.put("blog",blog);
        long userId = blog.getUser().getId();
        map.put("blogConut",blogService.getBlogCount(userId));
        List<Comment> comments = blog.getComments();
        map.put("comments",comments);
        //将热门文章放入
        List<Blog> titles = blogService.getFireBlog(1,5);
        System.out.println("热门文章--->"+titles);
        map.put("titles",titles);
        //保存评论数
        map.put("commentNumber",comments.size());
        return "blog/index";
    }

    /**
     * 编辑博客
     */
    @GetMapping("/edit/{username}")
    public String editBlog(@PathVariable("username")String username,Map<String,Object> map){
        User user = userService.findByUsername(username);
        List<BlogType> types = blogTypeService.fingByUserId(user.getId());
        map.put("types",types);
        map.put("user",user);
        System.out.println(types);
        return "blog/editBlog";
    }

    /**
     * 保存博客
     */
    @PostMapping("/save")
    @ResponseBody
    public String saveBlog(Blog blog,@RequestParam("typeId")Long typeId,@RequestParam("username")String username){
        System.out.println("保存的博客--->"+blog);
        System.out.println("保存的typeId--->"+typeId);
        System.out.println("保存的username--->"+username);
        Boolean b = blogService.addBlog(blog,typeId,username);
        if(b){
            return "ok";
        }else{
            return "no";
        }
    }

    /**
     * 用户博客首页
     */
    @GetMapping("/{username}/index")
    public String userIndex(@PathVariable("username")String username,@RequestParam(value = "uid",required = false,defaultValue = "0")Long uid,
                            @RequestParam(value= "page",required = false,defaultValue ="1")Integer page,
                            @RequestParam(value= "limit",required = false,defaultValue ="4")Integer limit,
                            Model model){
        User u = userService.findByUsername(username);
        uid = u.getId();
        model.addAttribute("user",u);
        System.out.println("查看用户的博客首页---------->"+username);
        //根据用户id查询改用户所有的博客
        List<Blog> blogs = blogService.getUserBlogPage(uid,page,limit);
        //保存博客
        model.addAttribute("blogs",blogs);
        //保存当前页码
        model.addAttribute("page",page);
        //保存数据库一共有多少条数据
        Long count = blogService.getBlogCount(u.getId());
        double pageConut = Math.ceil((double)count/limit);
        model.addAttribute("pageCount",(int)pageConut);
        //保存用户注册距离现在多长时间
        String day = DataFormat.getData(u.getCreateTime());
        model.addAttribute("day",day);
        //将标签放进HashSet 去重复
        HashSet<String> tags = new HashSet<String>();
        for(Blog b:blogs){
            String tag = b.getTags();
            if(tag!=null&&tag.length()>0){
                String[] tagArr = tag.split(",");
                for(String str:tagArr){
                    tags.add(str);
                }
            }
        }
        System.out.println("所有标签--->"+tags);
        model.addAttribute("tags",tags);
        return "blog/userMessage";
    }

    /**
     * 异步点赞
     */
    @GetMapping("/like/{bid}")
    @ResponseBody
    public String like(@PathVariable("bid")Long bid){
        if(bid==likeBlogId){
            return "error";
        }
        int status = blogService.dianZan(bid);
        if(status>0){
            likeBlogId = bid;
            return "success";
        }
        return "error";
    }

    /**
     * 异步评论
     */
    @PostMapping("/commont")
    @ResponseBody
    public Response commont(@RequestParam("uid")Long uid,@RequestParam("bid")Long bid,@RequestParam("contont")String contont){
        System.out.println("uid->"+uid+"----->bid-->"+bid+"-------->contonmt---->"+contont);
        Blog blog = commentService.saveComment(uid,bid,contont);
        if(blog!=null){
            return new Response(true,"评论成功",blog);
        }
        return new Response(false,"评失败",blog);
    }

    /**
     * 根据id删除
     */
    @GetMapping("/del/{id}")
    @ResponseBody
    public String delBlog(@PathVariable("id")Long bid){
        System.out.println("xxxxxxxxx");
        blogService.delBlog(bid);
        return "success";
    }

    /**
     * 删除评论
     */
    @GetMapping("/delComment/{id}")
    @ResponseBody
    public String delComment(@PathVariable("id")Long id){
        boolean b = commentService.delComment(id);
        if(b){
            return "success";
        }
        return "error";
    }

    //根据标题搜索
    @GetMapping("/search")
    public String search(@RequestParam(value = "page",required = false,defaultValue ="1")int page,
                           @RequestParam(value = "limit",required = false,defaultValue = "4")int limit,
                          @RequestParam(value = "title",required = false,defaultValue = "")String title,Map<String,Object> map){
        Pageable pageable =  PageRequest.of((page-1)*limit,limit);
        Page<Blog> pages = blogService.findByTitleLike(title, pageable);
        List<Blog> blogs = pages.getContent();
        map.put("blogs",blogs);
        map.put("page",page);
        double pageCount =Math.ceil((double)blogService.getCount()/limit);
        map.put("pageCount",(int)pageCount);
        //将标签放进HashSet 去重复
        HashSet<String> tags = new HashSet<String>();
        for(Blog b:blogs){
            String tag = b.getTags();
            if(tag!=null&&tag.length()>0){
                String[] tagArr = tag.split(",");
                for(String str:tagArr){
                    tags.add(str);
                }
            }
        }
        System.out.println("所有标签--->"+tags);
        map.put("tags",tags);
        //将热门用户头像放入
        List<String[]> avatars = userService.getAvatar();
        System.out.println("热门用户--->"+avatars);
        map.put("avatars",avatars);
        //将热门文章放入
        List<Blog> titles = blogService.getFireBlog(1,5);
        System.out.println("热门文章--->"+titles);
        map.put("titles",titles);
        return "/index2";
    }

    //根据标题搜索
    @GetMapping("/userSearch")
    public String userSearch(@RequestParam(value = "page",required = false,defaultValue ="1")int page,
                         @RequestParam(value = "limit",required = false,defaultValue = "4")int limit,
                         @RequestParam(value = "title",required = false,defaultValue = "")String title,Map<String,Object> map){
        Pageable pageable =  PageRequest.of((page-1)*limit,limit);
        Page<Blog> pages = blogService.findByTitleLike(title, pageable);
        List<Blog> blogs = pages.getContent();
        map.put("blogs",blogs);
        map.put("page",page);
        double pageCount =Math.ceil((double)blogService.getCount()/limit);
        map.put("pageCount",(int)pageCount);
        //将标签放进HashSet 去重复
        HashSet<String> tags = new HashSet<String>();
        for(Blog b:blogs){
            String tag = b.getTags();
            if(tag!=null&&tag.length()>0){
                String[] tagArr = tag.split(",");
                for(String str:tagArr){
                    tags.add(str);
                }
            }
        }
        System.out.println("所有标签--->"+tags);
        map.put("tags",tags);
        //将热门用户头像放入
        List<String[]> avatars = userService.getAvatar();
        System.out.println("热门用户--->"+avatars);
        map.put("avatars",avatars);
        //将热门文章放入
        List<Blog> titles = blogService.getFireBlog(1,5);
        System.out.println("热门文章--->"+titles);
        map.put("titles",titles);
        return "/index2";
    }


}
