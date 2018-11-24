package xin.l024.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig  extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index.html").setViewName("admin/index");
        registry.addViewController("/userIndex.html").setViewName("admin/userIndex");
        registry.addViewController("/blogIndex.html").setViewName("admin/blogIndex");
        registry.addViewController("/commentIndex.html").setViewName("admin/commentIndex");
        registry.addViewController("/setting.html").setViewName("admin/setting");
    }

    //配置静态资源文件路径
    //通过addResourceHandler添加映射路径，然后通过addResourceLocations来指定路径。我们访问自定义my文件夹中
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}
