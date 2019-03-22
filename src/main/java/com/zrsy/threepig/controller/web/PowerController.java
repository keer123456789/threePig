package com.zrsy.threepig.controller.web;

import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.web.PowerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限管理
 */
@RestController
public class PowerController {
    protected static final Logger logger = LoggerFactory.getLogger(PowerController.class);
    @Autowired
    PowerService powerService;

    @PostMapping("/addPower")
    public ParserResult addPower(@RequestBody Map map) {

        return powerService.addPower((Map) map.get("data"));
    }

    @GetMapping("/getPower")
    public ParserResult getPower() {
        return powerService.getPower();
    }

    @PostMapping("/fixPower")
    public ParserResult fixPower(@RequestBody Map map) {
        return powerService.fixPower((Map) map.get("data"));
    }

    @GetMapping("/deletePower/{powerId}")
    public ParserResult fixPower(@PathVariable String  powerId) {
        return powerService.deletePower(powerId);
    }

    @GetMapping("/getAllPowerId")
    public ParserResult getAllPowerId( ){
        return powerService.getAllPowerId();
    }



}
