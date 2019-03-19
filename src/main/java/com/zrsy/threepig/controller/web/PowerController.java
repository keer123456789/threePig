package com.zrsy.threepig.controller.web;

import com.zrsy.threepig.domain.ParserResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PowerController {
    protected static final Logger logger = LoggerFactory.getLogger(PowerController.class);

    @PostMapping("/addPower")
    public ParserResult addPower(@RequestBody Map map) {
        ParserResult parserResult =new ParserResult();
        parserResult.setMessage("success");
        logger.info(map.toString());
        return parserResult;
    }

    @GetMapping("/getPower")
    public ParserResult getPower(){
        ParserResult parserResult =new ParserResult();
        List<Map> list=new ArrayList<>();
        Map map=new HashMap();
        map.put("powerId","11");
        map.put("powerName","12");
        map.put("powerStatus","13");
        map.put("powerInfo","14");
        list.add(map);
        parserResult.setData(list);
        return parserResult;
    }

}
