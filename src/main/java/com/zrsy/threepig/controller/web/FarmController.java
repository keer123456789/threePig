package com.zrsy.threepig.controller.web;

import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.web.FarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 猪肉溯源的前端的请求，主要是养殖场的信息
 */
@RestController
public class FarmController {
    protected static final Logger logger = LoggerFactory.getLogger(FarmController.class);
    @Autowired
    FarmService farmService;

    /**
     * 设置养殖场的信息
     * @param map
     * @return
     */
    @PostMapping("/setFarmInfo")
    public ParserResult setFarmInfo(@RequestBody Map map){
        logger.info("接收到设置养殖场信息的请求……，"+map.toString());
        return farmService.setFarmInfo((Map)map.get("data"));
    }

    /**
     * 获取养殖场的信息
     * @param address
     * @return
     */
    @GetMapping("/getFarmInfo/{address}")
    public  ParserResult getFarmInfo(@PathVariable String address){
        logger.info("接收到获取养殖场信息的请求，"+address);
        return farmService.getFarmInfo(address);
    }

    /**
     * 设置数据密钥信息
     * @param key
     * @return
     */
    @PostMapping("/setBigchainDBKey")
    public ParserResult setBigchainDBKey( String key){
        logger.info("接收到设置数据密钥的请求……"+key);
        return farmService.setBigchainDBKey(key);
    }
}
