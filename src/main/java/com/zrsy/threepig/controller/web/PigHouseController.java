package com.zrsy.threepig.controller.web;

import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.web.PigHouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PigHouseController {

    protected static final Logger logger = LoggerFactory.getLogger(PigHouseController.class);

    @Autowired
    PigHouseService pigHouseService;
    /**
     *增加猪舍
     * @param map
     * @return
     */
    @PostMapping("/addPighouse")
    public ParserResult addPigHouse(@RequestBody Map map){
        return pigHouseService.addPigHouse((Map) map.get("data"));
    }

    /**
     * 获得所有猪舍信息
     * @return
     */
    @GetMapping("/pigHouseList")
    public ParserResult getPigHouseList(){
        return pigHouseService.getPigHouseList();
    }

    @GetMapping("/pigHouseIdList")
    public ParserResult getPighouseIDList(){
        return pigHouseService.getPigHouseIDList();
    }
}
