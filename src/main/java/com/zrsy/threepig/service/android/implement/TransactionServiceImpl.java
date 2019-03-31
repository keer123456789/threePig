package com.zrsy.threepig.service.android.implement;

import com.zrsy.threepig.BDQL.BDQLUtil;
import com.zrsy.threepig.BigchainDB.BigchainDBRunner;
import com.zrsy.threepig.Contract.PIG.Pig;
import com.zrsy.threepig.Contract.RBAC.User;
import com.zrsy.threepig.Util.ContractUtil;
import com.zrsy.threepig.Util.EthereumUtil;
import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.android.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static Logger logger= LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    EthereumUtil ethereumUtil;

    @Autowired
    ContractUtil contractUtil;



    /**
     * 获取地址余额
     * @param address
     * @return
     */
    @Override
    public ParserResult getBanlanceOf(String address) {
        ParserResult parserResult=new ParserResult();
        String blance=ethereumUtil.getBlance(address);
        parserResult.setData(blance);
        parserResult.setMessage("success");
        parserResult.setStatus(ParserResult.SUCCESS);
        return parserResult;
    }

    /**
     * 获取猪的信息，先用earid查出721ID，721ID再去用合约查询猪的信息
     * @param earId
     * @return
     */
    @Override
    public ParserResult getPigInfo(String earId) {
        ParserResult parserResult=new ParserResult();
        parserResult= BDQLUtil.work("select 721ID from pigInfo where earID="+earId);
        Map map=(Map) parserResult.getData();
        String id=map.get("721ID").toString();
        Pig pig=contractUtil.PigLoad();
        Tuple6<String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger> tuple6= null;
        try {
            tuple6 = pig.getPig(new BigInteger(id)).send();
        } catch (Exception e) {
            e.printStackTrace();
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setMessage("fail");
            logger.error("查询猪的信息失败！！");
            return parserResult;
        }
        Map result=new HashMap();
        result.put("address",tuple6.getValue1());
        result.put("birthtime",tuple6.getValue2().toString());
        result.put("breed",tuple6.getValue3().toString());
        result.put("721ID",tuple6.getValue4().toString());
        result.put("status",tuple6.getValue5().toString());
        result.put("pigHouse",tuple6.getValue6().toString());
        parserResult.setStatus(ParserResult.SUCCESS);
        parserResult.setMessage("success");
        parserResult.setData(map);
        logger.info("查询成功，"+map);
        return parserResult;
    }

    /**
     * 买家确认购买
     * @param address
     * @param id
     * @return
     */
    @Override
    public ParserResult confirmBuy(String address, String id) {
        ParserResult parserResult=new ParserResult();
        Pig pig=contractUtil.PigLoad(address);
        TransactionReceipt receipt= null;
        try {
            receipt = pig.confirmBuy(new BigInteger(id),new BigInteger("10")).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Pig.TransferEventResponse> list=pig.getTransferEvents(receipt);

        if(list.get(0).tokenId.toString().equals(id)&&list.get(0).status.intValue()==1){
            logger.info("确认购买成功");
            parserResult.setMessage("success");
            parserResult.setStatus(ParserResult.SUCCESS);
            return parserResult;
        }
        logger.error("确认购买失败！！");
        parserResult.setStatus(ParserResult.ERROR);
        parserResult.setMessage("fail");
        return parserResult;
    }

    /**
     * 卖家确认发货
     * @param fromAddress 卖家地址
     * @param toAddress 买家地址
     * @param id 猪721ID
     * @return
     */
    @Override
    public ParserResult transfer(String fromAddress, String toAddress, String id) {
        ParserResult parserResult=new ParserResult();
        Pig pig = contractUtil.PigLoad(fromAddress);
        TransactionReceipt receipt= null;
        try {
            receipt = pig.transfer(toAddress,new BigInteger(id)).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Pig.TransferEventResponse> list=pig.getTransferEvents(receipt);
        if(id.equals(list.get(0).tokenId.toString())&&list.get(0).status.intValue()==2){
            logger.info("确认发货成功");
            parserResult.setMessage("success");
            parserResult.setStatus(ParserResult.SUCCESS);
            return parserResult;
        }
        logger.error("确认发货失败！！");
        parserResult.setStatus(ParserResult.ERROR);
        parserResult.setMessage("fail");
        return parserResult;
    }

    /**
     * 买家确认收货
     * @param fromAddress
     * @param toAddress
     * @param id
     * @return
     */
    @Override
    public ParserResult changeStatus(String fromAddress, String toAddress, String id) {
        ParserResult parserResult=new ParserResult();
        Pig pig= contractUtil.PigLoad(fromAddress);
        TransactionReceipt receipt= null;
        try {
            receipt = pig.changeStatus(toAddress,new BigInteger(id),new BigInteger("10")).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Pig.TransferEventResponse> list=pig.getTransferEvents(receipt);
        if(list.get(0).tokenId.toString().equals(id)&&list.get(0).status.intValue()==3){
            logger.info("确认收货成功");
            parserResult.setMessage("success");
            parserResult.setStatus(ParserResult.SUCCESS);
            return parserResult;
        }
        logger.error("确认收货失败！！");
        parserResult.setStatus(ParserResult.ERROR);
        parserResult.setMessage("fail");
        return null;
    }

    public static void main(String[] args) {
        BigchainDBRunner.StartConn();
        BDQLUtil.work("insert into pig(id,name)values('123123','zhang')");
        ParserResult parserResult=BDQLUtil.work("select id from pig where name=zhang");
        System.out.println(parserResult.getData());
    }
}
