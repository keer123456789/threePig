package com.zrsy.threepig.Util;

import org.springframework.beans.factory.annotation.Value;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.util.List;

/**
 * 针对geth的personal接口的工具类
 */
public class EthereumUtil {
    @Value("${web3_url}")
    private String web3_url;

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
            newAccountIdentifier = web3j.personalNewAccount("22345678").send();
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
    public boolean UnlockAccount(String address,String password){
        Admin web3j = Admin.build(new HttpService(web3_url));

        PersonalUnlockAccount personalUnlockAccount=null;
        try {
            personalUnlockAccount=web3j.personalUnlockAccount(address,password).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return personalUnlockAccount.accountUnlocked();
    }

    public static void main(String[] args) {

    }

}
