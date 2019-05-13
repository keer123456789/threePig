package com.zrsy.threepig.service.web;

import com.zrsy.threepig.domain.ParserResult;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

public interface FarmService {
    ParserResult setFarmInfo(Map map);

    ParserResult getFarmInfo(String address);

    ParserResult setBigchainDBKey(String key);
}
