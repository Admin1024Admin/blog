package xin.l024.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class SendMailUtil {
    public boolean sendSimpleMailMessage(JavaMailSenderImpl javaMailSender,String title,String content,String email){
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            //标题
            mailMessage.setSubject(title);
            //正文
            mailMessage.setText(content);
            //发送给谁
            mailMessage.setTo(email);
            //谁发送的。要和配置里面的一样
            mailMessage.setFrom("409665838@qq.com");
            System.out.println("message------------->"+mailMessage+"---->javaMailSender--->"+javaMailSender);
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendMimeMessage(JavaMailSenderImpl javaMailSender,String title,String content,String email){
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            //标题
            helper.setSubject(title);
            //正文
            helper.setText(content,true);
            System.out.println("xxxxxxxxxxxxxxxxxxxx"+content);
            //发送给谁
            helper.setTo(email);
            //谁发送的。要和配置里面的一样
            helper.setFrom("409665838@qq.com");
            javaMailSender.send(mimeMessage);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
