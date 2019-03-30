package com.zrsy.threepig.service.android.implement;

import com.zrsy.threepig.Contract.PIG.Pig;
import com.zrsy.threepig.Util.ContractUtil;
import com.zrsy.threepig.Util.EthereumUtil;
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
    EthereumUtil ethereumUtil;


    @Override
    public ParserResult getBanlanceOf(String address) {
        ParserResult parserResult=new ParserResult();
        String blance=ethereumUtil.getBlance(address);
        parserResult.setData(blance);
        parserResult.setMessage("success");
        parserResult.setStatus(ParserResult.SUCCESS);
        return parserResult;

    }
}
