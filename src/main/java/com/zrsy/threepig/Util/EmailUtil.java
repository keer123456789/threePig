package com.zrsy.threepig.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 邮件的工具类
 */
@Component("emailtool")
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${sendEmailaccount}")
    private String emailAccount;


    public void sendSimpleMail(String toAddress){
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailAccount);
            helper.setTo(toAddress);
            helper.setSubject("智能养猪管理系统审核通知！！！！");

            StringBuffer sb = new StringBuffer();
            sb.append("<h1>恭喜您在智能养猪管理系统注册成功！！！</h1>")
                    .append("<p >附件中的两个密钥文件请妥善保管，不要丢失泄露！！</p>")
                    .append("<p >Ekeystore文件用于登录和交易使用</p>")
                    .append("<p >Bkeystore文件用于系统内设备使用</p>");
            helper.setText(sb.toString(), true);
            FileSystemResource fileSystemResource=new FileSystemResource(new File("./data/UTC--2019-03-22T12-57-00.319222052Z--60de16ea63fc458b6701830ba81d5a502e896ab9"));
            FileSystemResource fileSystemResource1=new FileSystemResource(new File("./keypair.txt"));
            helper.addAttachment("Ekeystore",fileSystemResource);
            helper.addAttachment("Bkeystore",fileSystemResource1);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

