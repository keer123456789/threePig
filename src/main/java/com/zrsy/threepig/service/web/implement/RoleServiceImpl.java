package com.zrsy.threepig.service.web.implement;

import com.zrsy.threepig.Contract.RBAC.User;
import com.zrsy.threepig.Util.ContractUtil;
import com.zrsy.threepig.domain.ParserResult;
import com.zrsy.threepig.service.web.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.tuples.generated.Tuple3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RoleServiceImpl 将角色的管理再合约上实现。
 */
@Service
public class RoleServiceImpl implements RoleService {
    protected static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    ContractUtil contractUtil;

    @Override
    public ParserResult addRole(Map map) {
        ParserResult parserResult = new ParserResult();
        User user = contractUtil.UserLoad();
        String a = map.get("roleId").toString();

        try {
            user.createRole(new BigInteger(a), map.get("roleFName").toString(), map.get("roleName").toString()).send();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("增加角色失败，角色信息：" + map.toString());
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setMessage("增加角色失败，角色信息：" + map.toString());
            return parserResult;
        }
        parserResult.setMessage("success");
        parserResult.setStatus(ParserResult.SUCCESS);
        return parserResult;
    }

    @Override
    public ParserResult getAllRole() {
        ParserResult parserResult = new ParserResult();
        User user = contractUtil.UserLoad();
        BigInteger count = null;
        try {
            count = user.getRoleCount().send();
        } catch (Exception e) {
            e.printStackTrace();
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setMessage("查询角色个数失败！！");
            return parserResult;
        }


        List<Map> list = new ArrayList<>();
        int sum = count.intValue();
        for (int i = 0; i < sum; i++) {
            String name = null;
            try {
                name = user.getRoleNameByIndex(new BigInteger(i + "")).send();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("未获取到第" + i + "个角色名称");
                continue;
            }
            Tuple3<List<BigInteger>, String, String> tuple3;
            try {
                tuple3 = user.getRoleInfo(name).send();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("获取角色名称:" + name + "失败！！");
                continue;
            }
            Map result = new HashMap();
            List<BigInteger> list1 = tuple3.getValue1();
            String a = "";
            for (BigInteger bigInteger : list1) {
                a = a + bigInteger.toString() + "/    ";
            }
            logger.info(a);
            result.put("roleId", tuple3.getValue1().toString());
            result.put("roleFName", tuple3.getValue2());
            result.put("roleName", tuple3.getValue3());

            list.add(result);
        }
        logger.info(list.toString());
        parserResult.setMessage("获取全部角色信息成功！");
        parserResult.setStatus(ParserResult.SUCCESS);
        parserResult.setData(list);
        return parserResult;
    }


    @Override
    public ParserResult changeRolePowerAndFName(Map map) {
        ParserResult parserResult = new ParserResult();
        User user = contractUtil.UserLoad();
        try {
            user.changeRoleIdAndFNameId(map.get("roleName").toString(), new BigInteger(map.get("roleId").toString()), map.get("fName").toString()).send();
        } catch (Exception e) {
            e.printStackTrace();
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setMessage("error");
        }
        parserResult.setMessage("success");
        parserResult.setStatus(ParserResult.SUCCESS);
        return parserResult;
    }
}
