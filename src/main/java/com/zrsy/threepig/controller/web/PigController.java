package com.zrsy.threepig.controller.web;

import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.web.PigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PigController {
    protected static final Logger logger = LoggerFactory.getLogger(PigController.class);

    @Autowired
    PigService pigService;

    /**
     * 增加新猪
     *
     * @param map
     * @return
     */
    @PostMapping("/addpig")
    public ParserResult addPig(@RequestBody Map map) {
        return pigService.addPig((Map) map.get("data"));
    }

    /**
     * 获得改养殖场中所有的猪的信息
     *
     * @return
     */
    @GetMapping("/getAllPig")
    public ParserResult getAllPig() {
        return pigService.getAllPig();
    }


    @RequestMapping(value = "/getPigInfo/{pigId}",method = RequestMethod.GET)
    public ParserResult getPigInfo(@PathVariable String pigId){
        return pigService.getPigInfo(pigId);
    }

    @RequestMapping(value = "/getPigList/{pigHouseId}",method = RequestMethod.GET)
    public ParserResult getPigList(@PathVariable String pigHouseId){
        return pigService.getPigList(pigHouseId);
    }


}
