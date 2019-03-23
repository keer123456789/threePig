package com.zrsy.threepig.controller.web;

import com.zrsy.threepig.Util.EmailUtil;
import com.zrsy.threepig.domain.ParserResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import java.util.Map;

/**
 * 用户管理
 */
@RestController
public class UserController {
    protected static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    EmailUtil emailUtil;

    /**
     * 提交审核信息
     * @param map
     * @return
     */
    @PostMapping("/register")
    public ParserResult register(@RequestBody Map map){
        ParserResult parserResult=new ParserResult();
        parserResult.setMessage("success");
        return parserResult;
    }

    @GetMapping("/sendMail")
    public void sendMail(){
        emailUtil.sendSimpleMail("15110074528@163.com");
    }

}
