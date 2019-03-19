package com.zrsy.threepig.service.web.implement;

import com.zrsy.threepig.BDQL.BDQLUtil;
import com.zrsy.threepig.BigchainDB.BigchainDBUtil;
import com.zrsy.threepig.domain.BDQL.BigchainDBData;
import com.zrsy.threepig.domain.BDQL.Table;
import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.web.PigHouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PigHouseServiceImpl implements PigHouseService {
    protected static final Logger logger = LoggerFactory.getLogger(PigHouseServiceImpl.class);

    /**
     * 增加猪舍信息。
     *
     * @param map
     * @return
     */
    @Override
    public ParserResult addPigHouse(Map map) {
        ParserResult parserResult = new ParserResult();
        BigchainDBData bigchainDBData = new BigchainDBData("pigHouseInfo", map);
        String assetID;
        try {
            assetID = BigchainDBUtil.createAsset(bigchainDBData);
        } catch (Exception e) {
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setMessage("error");
            e.printStackTrace();
            return parserResult;
        }
        logger.info("创建资产成功，资产ID：" + assetID);
        logger.info("添加新猪成功");
        parserResult.setStatus(ParserResult.SUCCESS);
        parserResult.setMessage("success");
        return parserResult;

    }

    /**
     * 查询所有的猪舍信息
     * @return
     */
    @Override
    public ParserResult getPigHouseList() {
        ParserResult parserResult = new ParserResult();
        parserResult = BDQLUtil.work("select * from pigHouseInfo");
        Table table = (Table) parserResult.getData();
        parserResult.setData(table.getData());
        logger.info(parserResult.getData().toString());
        return parserResult;
    }

    /**
     * 查询所有猪舍的ID号
     * @return
     */
    @Override
    public ParserResult getPigHouseIDList() {
        ParserResult parserResult = new ParserResult();
        parserResult = BDQLUtil.work("select pigstyId from pigHouseInfo");
        Table table = (Table) parserResult.getData();
        List<Map> list=table.getData();
        List<String> list1=new ArrayList<>();
        for(Map map:list){
            list1.add(map.get("pigstyId").toString().substring(0,4));
        }
        parserResult.setData(list1);
        logger.info(parserResult.getData().toString());
        return parserResult;
    }


}
