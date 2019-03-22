package com.zrsy.threepig.Util;

import ch.ethz.ssh2.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class SSHUtil {
    protected static final Logger logger = LoggerFactory.getLogger(SSHUtil.class);

    @Value("${ssh_ip}")
    private  String ip ;
    @Value("${userName}")
    private  String userName;
    @Value("${userPassward}")
    private  String password;

    private boolean login(){
        Connection conn = new Connection(ip);
        try {
            //连接远程服务器
            conn.connect();
            //使用用户名和密码登录
            conn.authenticateWithPassword(userName, password);
        } catch (IOException e) {
            logger.error("用户%s密码%s登录服务器%s失败！", userName, password, ip);
            e.printStackTrace();
            return false;
        }
        logger.info("用户%s密码%s登录服务器%s成功！", userName, password, ip);
        return true;

    }

    public static void main(String[] args) {
        SSHUtil sshUtil=new SSHUtil();
        sshUtil.login();
    }
}
