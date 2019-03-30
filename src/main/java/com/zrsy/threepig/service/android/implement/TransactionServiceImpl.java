package com.zrsy.threepig.service.android.implement;

import com.zrsy.threepig.Contract.PIG.Pig;
import com.zrsy.threepig.Util.ContractUtil;
import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.android.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import java.math.BigInteger;

@Service
public class TransactionServiceImpl implements TransactionService {
    private Logger logger= LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    ContractUtil contractUtil;


    @Override
    public ParserResult getBanlanceOf(String address) {
        ParserResult parserResult=new ParserResult();
        Pig pig=contractUtil.PigLoad(address);
        BigInteger blance=null;
        try {
            blance=pig.getBalanceOf().send();
        } catch (Exception e) {
            logger.error("获取余额失败！！");
            e.printStackTrace();
            parserResult.setMessage("faile");
            parserResult.setStatus(ParserResult.ERROR);
            return parserResult;
        }
        BigInteger value = Convert.toWei(blance.toString(), Convert.Unit.ETHER).toBigInteger();
        logger.info("获取余额成功，address:"+address+",blance:"+value.toString());
        parserResult.setStatus(ParserResult.SUCCESS);
        parserResult.setMessage("success");
        parserResult.setData(blance.toString());
        return parserResult;

    }
}
