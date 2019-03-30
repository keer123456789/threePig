package com.zrsy.threepig.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * 针对geth的personal接口的工具类
 */
@Component
public class EthereumUtil {
    @Value("${web3_url}")
    private String web3_url;

    @Value("${account_address}")
    private String from;

    private Logger logger= LoggerFactory.getLogger(EthereumUtil.class);
    /**
     * 创建新用户
     *
     * @param password 密码
     * @return address
     */
    public String createNewAccount(String password) {
        Admin web3j = Admin.build(new HttpService(web3_url));
        NewAccountIdentifier newAccountIdentifier = null;
        try {
            newAccountIdentifier = web3j.personalNewAccount(password).send();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return newAccountIdentifier.getAccountId();
    }

    /**
     * 获得系统内的所有用户地址
     * @return
     */
    public List<String> getAllAccount() {
        Admin web3j = Admin.build(new HttpService(web3_url));
        PersonalListAccounts listAccounts = null;
        try {
            listAccounts = web3j.personalListAccounts().send();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return listAccounts.getAccountIds();
    }

    /**
     * 解锁账户
     * @param address
     * @param password
     * @return
     */
    public Boolean UnlockAccount(String address,String password){
        Admin web3j = Admin.build(new HttpService(web3_url));
        PersonalUnlockAccount personalUnlockAccount=null;
        try {
            personalUnlockAccount=web3j.personalUnlockAccount(address,password).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(personalUnlockAccount.accountUnlocked()==null){
            return true;
        }
        return personalUnlockAccount.accountUnlocked();
    }

    /**
     * 解锁管理员账户
     * @return
     */
    public Boolean UnlockAccount(){
        Admin web3j = Admin.build(new HttpService(web3_url));
        PersonalUnlockAccount personalUnlockAccount=null;
        try {
            personalUnlockAccount=web3j.personalUnlockAccount(from,"11111111").send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(personalUnlockAccount.accountUnlocked()==null){
            return true;
        }
        return personalUnlockAccount.accountUnlocked();
    }
    /**
     * 新账户转账100eth
     * @param to
     */
    public boolean sendTransaction(String to){
        Admin web3j = Admin.build(new HttpService(web3_url));
        UnlockAccount(from,"11111111");
        BigInteger value = Convert.toWei("100.0", Convert.Unit.ETHER).toBigInteger();
        Transaction transaction=  Transaction.createEtherTransaction(from,null,BigInteger.valueOf(1) ,BigInteger.valueOf(99999999),to,value);
        try {
            EthSendTransaction ethSendTransaction=web3j.personalSendTransaction(transaction,"11111111").send();
            String hash=ethSendTransaction.getTransactionHash();
            if(!hash.equals(null)){
                return true;
            }
            else{
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取地址的余额
     * @param address
     * @return
     */
    public String  getBlance(String address){
        Web3j web3j=new JsonRpc2_0Web3j(new HttpService(web3_url));
        try {
            EthGetBalance ethGetBalance=web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            logger.info("获取地址为："+address+"的余额成功，余额为："+ethGetBalance.getBalance().toString());
            return ethGetBalance.getBalance().toString();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取地址为"+address+"的余额失败！");
            return null;
        }
    }

    public static void main(String[] args) throws IOException, CipherException {
        Credentials credentials = WalletUtils.loadCredentials("12345678", "keystore/0x4f35ae6c01aff6b750c1ff6a0404e40a348ca6dd/UTC--2019-03-26T08-59-55.561386305Z--4f35ae6c01aff6b750c1ff6a0404e40a348ca6dd");
        System.out.println(credentials.getAddress());
    }

}
