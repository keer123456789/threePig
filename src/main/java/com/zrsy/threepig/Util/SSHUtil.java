package com.zrsy.threepig.Util;

import ch.ethz.ssh2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * 工具类
 * 实现远程操作linux服务器
 */
public class SSHUtil {
    protected static final Logger logger = LoggerFactory.getLogger(SSHUtil.class);

    @Value("${ssh_ip}")
    private  String ip ;
    @Value("${userName}")
    private  String userName;
    @Value("${userPassward}")
    private  String password;

    private static String DEFAULTCHARTSET = "UTF-8";
    private static Connection conn;

    /**
     * 登录服务器
     * @return
     */
    private boolean login(){
        conn = new Connection("192.168.85.134");
        try {
            //连接远程服务器
            conn.connect();
            //使用用户名和密码登录
            if(conn.authenticateWithPassword("root", "root")){
                logger.info("用户%s密码%s登录服务器%s成功！", userName, password, ip);
                return true;
            }
            else{
                logger.error("用户%s密码%s登录服务器%s失败！", userName, password, ip);
                return false;
            }
        } catch (IOException e) {
            logger.error("用户%s密码%s登录服务器%s失败！", userName, password, ip);
            e.printStackTrace();
            return false;
        }



    }


    /**
     * 上传文件
     * @param conn
     * @param fileName 本地文件
     * @param remotePath 服务器目录
     */
    public void putFile(Connection conn, String fileName, String remotePath){
        SCPClient sc = new SCPClient(conn);
        try {
            //将本地文件放到远程服务器指定目录下，默认的文件模式为 0600，即 rw，
            //如要更改模式，可调用方法 put(fileName, remotePath, mode),模式须是4位数字且以0开头
            sc.put(fileName, remotePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * @param fileName 服务器文件
     * @param localPath 本地地址
     */
    public void copyFile( String fileName,String localPath){

        SCPClient sc = new SCPClient(conn);
        try {
            sc.get(fileName, localPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行shell脚本
     * @param cmd
     * @return
     */
    public static String execute(String cmd) {
        String result = "";
        try {
            Session session = conn.openSession();// 打开一个会话
            session.execCommand(cmd);// 执行命令
            result = processStdout(session.getStdout(), DEFAULTCHARTSET);
            // 如果为得到标准输出为空，说明脚本执行出错了
            if (result.equals("")) {
                result = processStdout(session.getStderr(), DEFAULTCHARTSET);
            }
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 执行shell脚本
     * @param cmd
     * @return
     */
    public static String executeSuccess(String cmd){
        String result = "";
        try {
            Session session = conn.openSession();// 打开一个会话
            session.execCommand(cmd);// 执行命令
            result = processStdout(session.getStdout(), DEFAULTCHARTSET);
//            conn.close();
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 处理脚本执行返回结果
     * @param in
     * @param charset
     * @return
     */
    private static String processStdout(InputStream in, String charset){
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public void closeConn(){
        conn.close();
    }





    public static void main(String[] args) {
        SSHUtil sshUtil=new SSHUtil();
        if(sshUtil.login());
        String a=sshUtil.execute("ls -l /root/ethereum/node/data/keystore/*60de16ea63fc458b6701830ba81d5a502e896ab9");
        String[] b=a.split("/");
        a=b[b.length-1];
        a=a.substring(0,a.length()-1);
        logger.info(a);
        sshUtil.copyFile("/root/ethereum/node/data/keystore/"+a,"./data");
    }
}
