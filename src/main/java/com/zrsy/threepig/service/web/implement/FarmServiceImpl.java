package com.zrsy.threepig.service.web.implement;

import com.bigchaindb.util.KeyPairUtils;
import com.zrsy.threepig.BDQL.BDQLUtil;
import com.zrsy.threepig.BigchainDB.BigchainDBUtil;
import com.zrsy.threepig.BigchainDB.KeyPairHolder;
import com.zrsy.threepig.controller.web.FarmController;
import com.zrsy.threepig.domain.BDQL.BigchainDBData;
import com.zrsy.threepig.domain.BDQL.Table;
import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.web.FarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FarmServiceImpl implements FarmService {
    protected static final Logger logger = LoggerFactory.getLogger(FarmServiceImpl.class);

    @Override
    public ParserResult setFarmInfo(Map map) {
        ParserResult parserResult = new ParserResult();
        BigchainDBData bigchainDBData = new BigchainDBData("farmInfo", map);
        logger.info("要增加的养殖场的信息   " + map.toString());
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
     * 获取相应信息的养殖场信息
     * @param address
     * @return
     */
    @Override
    public ParserResult getFarmInfo(String address) {
        ParserResult result = BDQLUtil.work("select * from farmInfo where address=" + address );
        Table table = (Table) result.getData();
        if (table.getData()!=null) {
            result.setData(table.getData().get(0));
            result.setMessage("success");
            result.setStatus(ParserResult.SUCCESS);
            logger.info("养殖场信息存在");
        }else{
            result.setMessage("fail");
            result.setStatus(ParserResult.ERROR);
            logger.info("养殖场信息不存在");
        }
        return result;
    }

    /**
     * 设置数据密钥，写入./keypair.txt
     *
     * @param key
     * @return
     */
    @Override
    public ParserResult setBigchainDBKey(String key) {
        ParserResult parserResult = new ParserResult();
        if (KeyPairHolder.SaveKeyPairToTXT(KeyPairHolder.getKeyPairFromString(key))) {
            parserResult.setStatus(ParserResult.SUCCESS);
            parserResult.setMessage("success");
            logger.info("设置数据密钥成功");
        } else {
            parserResult.setMessage("fail");
            parserResult.setStatus(ParserResult.ERROR);
            logger.error("设置数据密钥失败");
        }
        return parserResult;
    }
}
