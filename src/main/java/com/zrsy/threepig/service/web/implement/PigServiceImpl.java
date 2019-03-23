package com.zrsy.threepig.service.web.implement;

import com.zrsy.threepig.BDQL.BDQLUtil;
import com.zrsy.threepig.BigchainDB.BigchainDBUtil;
import com.zrsy.threepig.controller.web.PigController;
import com.zrsy.threepig.domain.BDQL.BigchainDBData;
import com.zrsy.threepig.domain.BDQL.Table;
import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.web.PigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PigServiceImpl implements PigService {
    protected static final Logger logger = LoggerFactory.getLogger(PigServiceImpl.class);

    /**
     * 增加新猪的信息
     *
     * @param info
     * @return
     */
    @Override
    public ParserResult addPig(Map info) {
        ParserResult parserResult = new ParserResult();
        BigchainDBData bigchainDBData = new BigchainDBData("pigInfo", info);
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
     * 获得猪场内全部猪的信息
     *
     * @return
     */
    @Override
    public ParserResult getAllPig() {
        ParserResult parserResult = new ParserResult();
        parserResult = BDQLUtil.work("select * from pigInfo");
        Table table = (Table) parserResult.getData();

        parserResult.setData(table.getData());
        logger.info(parserResult.getData().toString());
        return parserResult;
    }

    /**
     * 通过BDQL查询该猪的信息
     * @param earId 猪earID
     * @return
     */
    @Override
    public ParserResult getPigInfo(String earId) {
        ParserResult parserResult =new ParserResult();
        parserResult=BDQLUtil.work("select * from pigInfo where earId ="+earId);
        Table table = (Table)parserResult.getData();
        parserResult.setData(table.getData());
        logger.info(parserResult.getData().toString());
        return parserResult;
    }


    /**
     * 获得该猪舍内猪的信息列表
     * @param pigHouseId 猪舍号
     * @return
     */
    @Override
    public ParserResult getPigList(String pigHouseId) {
        ParserResult parserResult=BDQLUtil.work("select earId,breed,column,ringNumber,matingWeek,remarks from pigInfo where pigstyId ="+pigHouseId);
        Table table = (Table)parserResult.getData();
        parserResult.setData(table.getData());
        logger.info(parserResult.getData().toString());
        return parserResult;
    }
}