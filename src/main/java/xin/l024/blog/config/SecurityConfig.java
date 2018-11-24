package xin.l024.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置类
 * 启动web安全注解
 */
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法安全设置
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();   // 使用 BCrypt 加密
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder); // 设置密码加密方式
        return authenticationProvider;
    }
    /**
     * 自定义配置
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                //,"/user/**","/space/**","/blog/**"
                .antMatchers("/css/**","/bootstrap3.3.7/**","/static/**","/js/**,","/fonts/**","/index2","/index","/images/**","/layui/**","/backPassword","/getCode").permitAll()//这些都可以无权限访问
                .antMatchers("/user/**","/space/**","/blog/**").authenticated()//登录认证后才可以访问
                .antMatchers("/admins/**","/druid/*").hasRole("ADMIN")//users请求下的必须有admin角色才可以访问
                .and()
                .formLogin().usernameParameter("username").passwordParameter("password") //基于表单的验证
                .loginPage("/login").failureUrl("/login-error")//自定义登录页面和错误页面
                .and()
                .rememberMe().key("xin.l024").rememberMeParameter("remeber")
                .and()
                .exceptionHandling().accessDeniedPage("/403");
        //关闭跨域防护
        httpSecurity.csrf().disable();
        //退出返回的页面
        httpSecurity.logout().logoutSuccessUrl("/index2");
        //springSecurty使用X-Frame-Options防止网页被Frame
        httpSecurity.headers().frameOptions().disable();
    }

    /**
     * 认证信息管理
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }
}
