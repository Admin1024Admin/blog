package xin.l024.blog.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig {
    //为了让在yml配置文件中的参数生效，需要注入一个DataSource
    //配置数据库参数
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }
    //配置Druid监控
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map<String,String> initParams = new HashMap<String,String>();
        initParams.put("loginUsername","admin");
        initParams.put("loginPassword","123");
        //initParams.put("allow","");//可以配置只有谁可以访问，默认就是所有都可以访问
       // initParams.put("deny","192.168.12.10");//配置谁不可以访问
        bean.setInitParameters(initParams);//把初始化参数放入Servlet中
        return bean;
    }
    //配置Druid过滤器
    //配置一个web监控的Filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        Map<String,String> initParams = new HashMap<String,String>();
        initParams.put("exclusions","*.js,*.css,/druid/*");//设置不拦截的静态资源和请求
        bean.setInitParameters(initParams);
        bean.setFilter(new WebStatFilter());
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }
}
