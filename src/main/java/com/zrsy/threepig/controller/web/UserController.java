package com.zrsy.threepig.controller.web;

import com.zrsy.threepig.Util.EmailUtil;
import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.web.UserService;
import com.zrsy.threepig.service.web.implement.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * 用户管理
 */
@RestController
public class UserController {
    protected static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    EmailUtil emailUtil;
    @Autowired
    UserService userService;

    /**
     * 提交审核信息
     * @param map
     * @return
     */
    @PostMapping("/register")
    public ParserResult register(@RequestBody Map map){
        return userService.register((Map)map.get("data"));
    }




    @PostMapping("/login")
    public ParserResult  login(@RequestBody Map map, HttpServletResponse response) throws IOException {
        Map map1= (Map) map.get("data");
        ParserResult parserResult=userService.login(map1);
        if(parserResult.getStatus()==ParserResult.SUCCESS){
            Cookie cookie=new Cookie("userid",map1.get("account").toString());
            Cookie cookie1=new Cookie("password",map1.get("password").toString());
            response.addCookie(cookie);
            response.addCookie(cookie1);
        }
        return parserResult;
    }

    @PostMapping("/eroll")
    public ParserResult eroll(@RequestBody Map map){
        return userService.eroll((Map)map.get("data"));
    }

    @GetMapping("/getAllUser")
    public ParserResult getAllUser(){
        return userService.getAllUser();
    }

    @RequestMapping(value = "/banUser/{address}", method = RequestMethod.GET)
    public ParserResult banUser(@PathVariable String address){
        logger.info(address);
        return userService.banUser(address);
    }



    private String requestUri(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (request.getQueryString() != null) {
            requestUri += ("?" + request.getQueryString());
        }
        return "[" + request.getMethod() + "] " + requestUri;

    }


    private String requestBody(BufferedReader body) {
        String inputLine;
        String bodyStr = "";
        try {
            while ((inputLine = body.readLine()) != null) {
                bodyStr += inputLine;
            }
            body.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return "[body] " + bodyStr;
    }

}
