package com.zrsy.threepig.service.web.implement;

import com.zrsy.threepig.Contract.RBAC.User;

import com.zrsy.threepig.Util.ContractUtil;
import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.web.PowerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.tuples.generated.Tuple4;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PowerServiceImpl implements PowerService {
    protected static final Logger logger = LoggerFactory.getLogger(PowerServiceImpl.class);
    @Autowired
    ContractUtil contractUtil;

    /**
     * 增加权限
     *
     * @param map
     * @return
     */
    @Override
    public ParserResult addPower(Map map) {
        ParserResult parserResult = new ParserResult();
        User user = contractUtil.UserLoad();
        try {
            user.addPower(new BigInteger(map.get("powerId").toString()), map.get("powerName").toString(), map.get("powerInfo").toString()).send();
            parserResult.setMessage("success");
            parserResult.setStatus(ParserResult.SUCCESS);
            logger.info("增加权限成功！！！！" + map.toString());
        } catch (Exception e) {
            e.printStackTrace();
            parserResult.setMessage("fail");
            parserResult.setStatus(ParserResult.ERROR);
            logger.error("增加权限失败！！！！" + map.toString());
        }

        return parserResult;
    }

    /**
     * 获得全部权利信息
     *
     * @return
     */
    @Override
    public ParserResult getPower() {
        ParserResult parserResult = new ParserResult();
        User user = contractUtil.UserLoad();
        List<BigInteger> list = new ArrayList();
        try {
            list = user.getAllPowerID().send();
            logger.info("获得全部权限ID成功！！" + list.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获得全部权限ID失败！！");
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setMessage("获得全部权限ID失败！！");
            return parserResult;
        }
        List<Map> data = new ArrayList<>();
        for (BigInteger bigInteger : list) {
            Tuple4<BigInteger, String, String, Boolean> tuple4 = null;
            try {
                tuple4 = user.getPowerInfoBypowerId(bigInteger).send();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("获取权限号为" + bigInteger.toString() + "的权限信息失败！！！");
            }
            Map map = new HashMap();
            map.put("powerId", tuple4.getValue1().toString());
            map.put("powerName", tuple4.getValue2());
            map.put("powerInfo", tuple4.getValue3());
            if (tuple4.getValue4()) {
                map.put("powerStatus", "使用中");
            }
            data.add(map);
        }
        parserResult.setStatus(ParserResult.SUCCESS);
        parserResult.setMessage("获取权限信息成功");
        parserResult.setData(data);
        return parserResult;

    }

    /**
     * 修改权限信息
     *
     * @param map
     * @return
     */
    @Override
    public ParserResult fixPower(Map map) {
        ParserResult parserResult = new ParserResult();
        User user = contractUtil.UserLoad();
        try {
            user.changePowerInfo(new BigInteger(map.get("powerId").toString()), map.get("powerInfo").toString()).send();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改权利号：" + map.get("powerId").toString() + "的权利信息失败！！" + map.toString());
            parserResult.setMessage("修改权利号：" + map.get("powerId").toString() + "的权利信息失败！！" + map.toString());
            parserResult.setStatus(ParserResult.ERROR);
            return parserResult;
        }
        try {
            user.changePowername(new BigInteger(map.get("powerId").toString()), map.get("powerName").toString()).send();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改权利号：" + map.get("powerId").toString() + "的权利名称失败！！" + map.toString());
            parserResult.setMessage("修改权利号：" + map.get("powerId").toString() + "的权利名称失败！！" + map.toString());
            parserResult.setStatus(ParserResult.ERROR);
            return parserResult;
        }
        parserResult.setStatus(ParserResult.SUCCESS);
        parserResult.setMessage("success");
        return parserResult;
    }

    @Override
    public ParserResult deletePower(String powerId) {
        ParserResult parserResult=new ParserResult();
        User user = contractUtil.UserLoad();
        try {
            user.changeUnUse(new BigInteger(powerId)).send();
        } catch (Exception e) {
            e.printStackTrace();
            parserResult.setMessage("禁用权限失败！！！");
            parserResult.setStatus(ParserResult.ERROR);
            logger.error("禁用权限:"+powerId+"失败！！！");
            return parserResult;
        }
        parserResult.setStatus(ParserResult.SUCCESS);
        parserResult.setMessage("success");
        logger.info("禁用权限:"+powerId+"成功！！！");
        return parserResult;
    }

    @Override
    public ParserResult getAllPowerId() {
        ParserResult parserResult=new ParserResult();
        User user = contractUtil.UserLoad();
        List<BigInteger> list=null;
        try {
            list=user.getAllPowerID().send();
        } catch (Exception e) {
            e.printStackTrace();
            parserResult.setMessage("获取全部权限ID失败");
            parserResult.setStatus(ParserResult.ERROR);
            return parserResult;
        }
        parserResult.setStatus(ParserResult.SUCCESS);
        parserResult.setMessage("success");
        parserResult.setData(list);
        logger.info(list.toString());
        return parserResult;
    }
}
