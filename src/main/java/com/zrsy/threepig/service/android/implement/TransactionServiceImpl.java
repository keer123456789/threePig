package com.zrsy.threepig.service.android.implement;

import com.zrsy.threepig.BDQL.BDQLUtil;
import com.zrsy.threepig.BigchainDB.BigchainDBRunner;
import com.zrsy.threepig.Contract.PIG.Pig;
import com.zrsy.threepig.Util.ContractUtil;
import com.zrsy.threepig.Util.EthereumUtil;
import com.zrsy.threepig.domain.BDQL.Table;
import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.android.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    EthereumUtil ethereumUtil;

    @Autowired
    ContractUtil contractUtil;


    /**
     * 获取地址余额
     *
     * @param address
     * @return
     */
    @Override
    public ParserResult getBanlanceOf(String address) {
        ParserResult parserResult = new ParserResult();
        String blance = ethereumUtil.getBlance(address);
        parserResult.setData(blance);
        parserResult.setMessage("success");
        parserResult.setStatus(ParserResult.SUCCESS);
        return parserResult;
    }

    /**
     * 获取猪的信息，先用earid查出721ID，721ID再去用合约查询猪的信息
     *
     * @param earId
     * @return
     */
    @Override
    public ParserResult getPigInfo(String earId) {
        ParserResult parserResult = new ParserResult();
        parserResult = BDQLUtil.work("select tokenId from pigInfo where earId=" + earId);
        Table table = (Table) parserResult.getData();
        Map map = table.getData().get(0);
        String id = map.get("tokenId").toString();
        Pig pig = contractUtil.PigLoad();
        Tuple6<String, BigInteger, String, BigInteger, BigInteger, BigInteger> tuple6 = null;
        try {
            tuple6 = pig.getPig(new BigInteger(id)).send();
        } catch (Exception e) {
            e.printStackTrace();
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setMessage("fail");
            logger.error("查询猪的信息失败！！");
            return parserResult;
        }
        Map result = new HashMap();
        result.put("address", tuple6.getValue1());
        result.put("birthtime", tuple6.getValue2().toString());
        result.put("breed", tuple6.getValue3());
        result.put("tokenId", id);
        result.put("status", tuple6.getValue5().toString());
        result.put("pigHouse", tuple6.getValue6().toString());
        parserResult.setStatus(ParserResult.SUCCESS);
        parserResult.setMessage("success");
        parserResult.setData(result);
        logger.info("查询成功，" + result);
        return parserResult;
    }

    /**
     * 买家确认购买
     *
     * @param address
     * @param id
     * @return
     */
    @Override
    public ParserResult confirmBuy(String address, String id, String password) {
        ParserResult parserResult = new ParserResult();
        Pig pig = contractUtil.PigLoad(address);
        TransactionReceipt receipt = null;
        if (ethereumUtil.UnlockAccount(address, password)) {
            try {
                receipt = pig.confirmBuy(new BigInteger(id), new BigInteger("10000000000000000000")).send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Pig.Log_ConfirmBuyEventResponse> list = pig.getLog_ConfirmBuyEvents(receipt);

            if (list.get(0).tokenId.toString().equals(id) && list.get(0).status.intValue() == 2) {
                logger.info("确认购买成功");
                parserResult.setMessage("success");
                parserResult.setStatus(ParserResult.SUCCESS);
                return parserResult;
            }
        }
        logger.error("确认购买失败！！");
        parserResult.setStatus(ParserResult.ERROR);
        parserResult.setMessage("fail");
        return parserResult;
    }

    /**
     * 卖家确认发货
     *
     * @param fromAddress 卖家地址
     * @param toAddress   买家地址
     * @param id          猪721ID
     * @return
     */
    @Override
    public ParserResult transfer(String fromAddress, String toAddress, String id, String password) {
        ParserResult parserResult = new ParserResult();
        Pig pig = contractUtil.PigLoad(fromAddress);

        TransactionReceipt receipt = null;
        if (ethereumUtil.UnlockAccount(fromAddress, password)) {
            try {
                receipt = pig.transfer(toAddress, new BigInteger(id)).send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Pig.Log_TransferEventResponse> list = pig.getLog_TransferEvents(receipt);
            if (id.equals(list.get(0).tokenId.toString()) && list.get(0).status.intValue() == 3) {
                logger.info("确认发货成功");
                parserResult.setMessage("success");
                parserResult.setStatus(ParserResult.SUCCESS);
                return parserResult;
            }
        }
        logger.error("确认发货失败！！");
        parserResult.setStatus(ParserResult.ERROR);
        parserResult.setMessage("fail");
        return parserResult;
    }

    /**
     * 买家确认收货
     *
     * @param fromAddress
     * @param toAddress
     * @param id
     * @return
     */
    @Override
    public ParserResult changeStatus(String fromAddress, String toAddress, String id, String password) {
        ParserResult parserResult = new ParserResult();
        Pig pig = contractUtil.PigLoad(fromAddress);
        TransactionReceipt receipt = null;
        if (ethereumUtil.UnlockAccount(fromAddress, password)) {
            try {
                receipt = pig.changeStatus(toAddress, new BigInteger(id)).send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Pig.Log_ChangeStatusEventResponse> list = pig.getLog_ChangeStatusEvents(receipt);
            if (list.get(0).tokenId.toString().equals(id) && list.get(0).status.intValue() == 4) {
                logger.info("确认收货成功");
                parserResult.setMessage("success");
                parserResult.setStatus(ParserResult.SUCCESS);
                return parserResult;
            }
        }
        logger.error("确认收货失败！！");
        parserResult.setStatus(ParserResult.ERROR);
        parserResult.setMessage("fail");
        return null;
    }

    /**
     * 代售
     *
     * @param tokenId 721id
     * @param address
     * @return
     */
    @Override
    public ParserResult preSale(String tokenId, String address, String password) {
        ParserResult parserResult = new ParserResult();
        Pig pig = contractUtil.PigLoad(address);
        TransactionReceipt receipt = null;
        if (ethereumUtil.UnlockAccount(address, password)) {
            try {
                receipt = pig.preSale(new BigInteger(tokenId)).send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Pig.Log_PreSaleEventResponse> list = pig.getLog_PreSaleEvents(receipt);
            if (list.get(0).tokenId.toString().equals(tokenId) && list.get(0).status.intValue() == 1) {
                logger.info("代售成功");
                parserResult.setMessage("success");
                parserResult.setStatus(ParserResult.SUCCESS);
                return parserResult;
            }
        }
        logger.error("代售失败！！");
        parserResult.setStatus(ParserResult.ERROR);
        parserResult.setMessage("fail");
        return null;
    }

    public static void main(String[] args) {
        BigchainDBRunner.StartConn();
        BDQLUtil.work("insert into pig(id,name)values('123123','zhang')");
        ParserResult parserResult = BDQLUtil.work("select id from pig where name=zhang");
        System.out.println(parserResult.getData());
    }
}
