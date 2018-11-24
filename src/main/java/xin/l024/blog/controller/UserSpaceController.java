package xin.l024.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import xin.l024.blog.dto.Response;
import xin.l024.blog.entity.User;
import xin.l024.blog.service.UserService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/space")
public class UserSpaceController {
    @Autowired
    private UserService userService;

    /**
     *   跳转到个人设置页面
     */
    @GetMapping("/{username}/setting")
    @PreAuthorize("authentication.name.equals(#username)")
    public String setting(@PathVariable("username")String username,Map<String,Object> map){
        System.out.println("username---->"+username);
        User user = userService.findByUsername(username);
        map.put("user",user);
        return "users/setting";
    }

    /**
     * 更新头像
     */
    @PostMapping("/{username}/updataAvatar")
    @PreAuthorize("authentication.name.equals(#username)")
    @ResponseBody
    public Response updataAvatar(@PathVariable("username")String username,@RequestParam("imgUrl")String imgUrl){
        System.out.println("username---->"+username+"------imgUrl----->"+imgUrl);
        User user = userService.findByUsername(username);
        user.setAvatar(imgUrl);
        User u = userService.saveOrUpdataUser(user);
        if(u!=null){
            return new Response(true,"更新成功","");
        }
        return new Response(false,"更新失败","");
    }

    /**
     * 头像上传
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    @ResponseBody
    public Map<String,Object> uploadAvatar(@RequestParam("image")String image){
        String imgBase64 = image.replace("data:image/png;base64,","");
        boolean b = GenerateImage(imgBase64);
        System.out.println("保存成功---->"+b);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("result","ok");
        map.put("file","58676_b.jpg");
       return map;
    }


    //base64字符串转化成图片   
     public boolean GenerateImage(String imgStr){
     //对字节数组字符串进行Base64解码并生成图片       
         if (imgStr == null) { //图像数据为空           
             return false;
         }
         try{
             BASE64Decoder decoder = new BASE64Decoder();
            // Base64解码           
             byte[] b = decoder.decodeBuffer(imgStr);
             for(int i=0;i<b.length;++i)
             {
                if(b[i]<0){//调整异常数据                   
                    b[i]+=256;
                }
             }
             //生成jpeg图片           
             String imgFilePath = "D:\\images\\new.jpg";//新生成的图片           
             OutputStream out = new FileOutputStream(imgFilePath);
             out.write(b);
             out.flush();
             out.close();
             return true;
         }
         catch (Exception e){
            return false;
         }
     }
}
