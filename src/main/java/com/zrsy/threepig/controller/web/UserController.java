package com.zrsy.threepig.controller.web;

import com.zrsy.threepig.domain.ParserResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户管理
 */
@RestController
public class UserController {
    protected static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ParserResult register(@RequestBody Map map){
        ParserResult parserResult=new ParserResult();
        parserResult.setMessage("success");
        return parserResult;
    }

}
