package com.zrsy.threepig.controller.hardware;


import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.hardware.IConnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
public class ConnController {
    protected static final Logger logger = LoggerFactory.getLogger(ConnController.class);
    @Autowired
    private ApplicationContext publisher;

    private String raspberryIP = null;

    private HashSet<String> nodeMcuIPs =new HashSet<>();

    @Autowired
    IConnService connService;

    /**
     * 树莓派发来请求，设置ip
     * @param request
     * @return
     */
    @GetMapping("/setRaspberryIP")
    public boolean setRaspberryIP(HttpServletRequest request) {
        raspberryIP = getIpAddr(request);
        if (!raspberryIP.equals(null)) {
            logger.info( raspberryIP);
            return true;
        } else {
            return false;
        }
    }

    /**
     * nodemcu请求服务器，获取ip，每次nodemcu最多可以3个
     * @param request
     * @return
     */
    @GetMapping("/setNodeMCUIP")
    public boolean setNodeMCUIP(HttpServletRequest request){
        if(nodeMcuIPs.size()>=3){
            nodeMcuIPs.clear();
            return false;
        }else{
            nodeMcuIPs.add(getIpAddr(request));
            logger.info(nodeMcuIPs.toString());
            return true;
        }
    }


    /**
     * nodemcu获取树莓派ip
     * @return
     */
    @GetMapping("/getRaspberryIP")
    public String getRaspberryIP(){
        return raspberryIP;
    }


    /**
     * 前端请求BigchainDB和变量中的树莓派信息
     * @return
     */
    @GetMapping("/getAllRaspberry")
    public ParserResult getAllRaspberry(){
        List list=connService.getAllRaspberry();

        return null;
    }

    /**
     * 前端获得当前内存中已经发来的nodemcuIP
     * @return
     */
    @GetMapping("/getNodeMcuIp")
    public ParserResult getNodeMcuIp(){
        logger.info("获得nodemcuIp的请求………………");
        nodeMcuIPs.add("192.168.1.102");
        nodeMcuIPs.add("192.168.1.103");
        nodeMcuIPs.add("192.168.1.104");
        ParserResult result = new ParserResult();
        result.setData(nodeMcuIPs);
        result.setMessage("success");
        result.setStatus(ParserResult.SUCCESS);
        return result;
    }

    /**
     * 前端发来请求，将资产信息发来，树莓派表 Raspberry
     * @param map
     * @return
     */
    @PostMapping("/createRaspberryAsset")
    public ParserResult createRaspberryAsset(@RequestBody Map map){
        logger.info(map.toString());
        map= (Map) map.get("data");
        return connService.createRaspberryAsset(map);
    }


    /**
     * 获取请求IP
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

}
