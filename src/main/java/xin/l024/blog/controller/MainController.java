package xin.l024.blog.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import xin.l024.blog.entity.Authority;
import xin.l024.blog.entity.Blog;
import xin.l024.blog.entity.User;
import xin.l024.blog.service.AuthorityService;
import xin.l024.blog.service.BlogService;
import xin.l024.blog.service.SettingService;
import xin.l024.blog.service.UserService;
import xin.l024.blog.util.DataFormat;
import xin.l024.blog.util.SendMailUtil;
import xin.l024.blog.util.SettingUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 主页控制器.
 * 
 * @since 1.0.0 2017年3月8日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
@Controller
public class MainController {
	
	private static final Long ROLE_USER_AUTHORITY_ID = 2L;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private SettingService settingService;

	@Autowired
	private JavaMailSenderImpl javaMailSender;

	@Autowired
	DefaultKaptcha defaultKaptcha;

	//时间错
	private String date="";

	private final Log log = LogFactory.getLog(MainController.class);
	@GetMapping(value = {"/","index"})
	public String root() {
		return "index";
	}
	
	@GetMapping("/index2")
	public String index(HttpSession session,Map<String, Object> map) {
		List<Blog> blogs = blogService.blogPage(0,4);
		map.put("blogs",blogs);
		map.put("page",1);
		//将网站名称放入session
		session.setAttribute("netName",settingService.getSetting().getTitle());
		double pageCount =Math.ceil(blogService.getCount()/4);
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
		return "index2";
	}

	/**
	 * 获取登录界面
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		model.addAttribute("errorMsg", "登陆失败，账号或者密码错误！");
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	/**
	 * 注册用户

	 */
	@PostMapping("/register")
	public String registerUser(User user) {
		List<Authority> authorities = new ArrayList<>();
		authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
		user.setAuthorities(authorities);
		System.out.println(user+"---------");
		//加密密码
		user.setEncodePassword(user.getPassword());
		userService.saveOrUpdataUser(user);
		return "redirect:/login";
	}
	
	@GetMapping("/search")
	public String search() {
		return "search";
	}

	//跳转发送邮件页面
	@GetMapping("/backPassword")
	public String backPassword(){
		return "/backPassword";
	}

	//发送邮件
	@PostMapping("/sendEmail")
	public String sendEmail(@RequestParam("email")String email,Map<String,Object> map){
		log.info(email);
		date = DataFormat.getMiss();
		SendMailUtil sendMailUtil = new SendMailUtil();
		boolean b2 = "".equals(date)?false:true;
		String content = "请点击链接完成验证:<a href='http://localhost:8080/checkEmail?email="+email+"&d="+date+"'>"+"http://localhost:8080/checkEmail?email="+email+"&d="+date+"</a>";
		log.info(content);
		boolean b = sendMailUtil.sendMimeMessage(javaMailSender,"天涯海角博客",content,email);
		b = b2==true?true:false;
		if(b){
			Object message = "验证信息已经发送到你的邮箱"+email+"，请前往你的邮箱进行验证....";
			map.put("message",message);
		}else{
			Object message = "验证信息发送到你的邮箱"+email+"出现了错误，请确定你的邮箱是否存在或者请稍后再试....";
			map.put("message",message);
		}
		return "/sendEmail";

	}

	//验证邮箱
	@GetMapping("/checkEmail")
	public String checkEmail(@RequestParam("email")String email,@RequestParam("d")String d,Map<String,Object> map){
		log.info("验证的时间--->"+d);
		if(date.equals(d)){
			User user = userService.checkEmail(email);
			log.info("邮箱查询出来的用户--->"+user);
			map.put("user",user);
			date = DataFormat.getMiss();
			return "checkSuccess";
		}else{
			return "checkError";
		}
	}

	//更新密码
	@PostMapping("/newPassword")
	public String newPassword(User user){
		log.info("更新密码的用户为--->"+user);
		User u = userService.getUserById(user.getId());
		//加密密码
		u.setEncodePassword(user.getPassword());
		//保存
		User u2 = userService.saveOrUpdataUser(u);
		if(u2!=null){
			return "redirect:/login";
		}
		return "error";
	}

	//注册的时候异步检查是否有注册过得用户 用户名 邮箱
	@GetMapping("/check")
	@ResponseBody
	public boolean check(User user){
		log.info("user--->"+user);
		if(user.getName()!=null){
			User u = userService.checkName(user.getName());
			if(u!=null){
				return false;
			}
			else {
				return true;
			}
		}else if(user.getUsername()!=null){
			User u = userService.findByUsername(user.getUsername());
			if(u!=null){
				return false;
			}
			else {
				return true;
			}
		}else if(user.getEmail()!=null){
			User u = userService.checkEmail(user.getEmail());
			if(u!=null){
				return false;
			}
			else {
				return true;
			}
		}else{
			return false;
		}
	}

	//获取验证码
	@RequestMapping("/getCode")
	public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		try {
			//生产验证码字符串并保存到session中
			String createText = defaultKaptcha.createText();
			httpServletRequest.getSession().setAttribute("vrifyCode", createText);
			//使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
			BufferedImage challenge = defaultKaptcha.createImage(createText);
			ImageIO.write(challenge, "jpg", jpegOutputStream);
		} catch (IllegalArgumentException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		//定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		httpServletResponse.setHeader("Cache-Control", "no-store");
		httpServletResponse.setHeader("Pragma", "no-cache");
		httpServletResponse.setDateHeader("Expires", 0);
		httpServletResponse.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream =
				httpServletResponse.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();
	}

	//验证码验证
	@RequestMapping("/checkCode")
	@ResponseBody
	public boolean imgvrifyControllerDefaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
		String captchaId = (String) httpServletRequest.getSession().getAttribute("vrifyCode");
		String parameter = httpServletRequest.getParameter("code");
		log.info("Session  vrifyCode ---->"+captchaId+"---- form code --->"+parameter);
		if (!captchaId.equals(parameter)) {
			log.info("错误的验证码");
			return false;
		} else {
			return true;
		}
	}


}
