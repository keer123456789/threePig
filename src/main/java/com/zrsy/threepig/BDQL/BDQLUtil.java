package com.zrsy.threepig.BDQL;


import com.zrsy.threepig.domain.ParserResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 本类是BDQL的分析工作的开始和一些工具
 *
 * work方法是开始函数
 */
public class BDQLUtil {
    public static final int ONE = 1;
    public static final int TWO = 2;


    private static Logger logger = LoggerFactory.getLogger(BDQLUtil.class);

    /**
     * 字符串小写转大写
     *
     * @param bdql
     * @return
     */
    public static String lowercaseToUpperCase(String bdql) {
        StringBuffer s = new StringBuffer();
        char c[] = bdql.toCharArray();
        for (int i = 0; i < bdql.length(); i++) {

            if (c[i] >= 97 && c[i] <= 122) {
                s.append((c[i] + "").toUpperCase());
            } else {
                s.append(c[i]);
            }
        }
        logger.info("字符串：" + bdql + ",转换后：" + s);
        return s.toString();
    }

    /**
     * 去掉字符串的第一个和最后一个单引号
     *
     * @param s
     * @return
     */
    public static String fixString(String s) {
        if (s.substring(0, 1).equals("'") && s.substring(s.length() - 1, s.length()).equals("'")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    /**
     * 获得BDQL的查询类型，select，insert，update,update and insert
     *
     * @param BDQL
     * @return 0, 1, 2
     */
    public static int getSort(String BDQL) {
        int sum = BDQL.split(";").length;
        if (sum == 1) {
            return 1;

        } else if (sum == 2) {
            String[] ss = BDQL.split(";");
            if (ss[0].indexOf("INSERT") != -1 && ss[1].indexOf("UPDATE") != -1) {
                logger.info("BDQL语句：" + BDQL + ",类型：插入 更新");
                return 2;
            } else {
                logger.error("BDQL语句：" + BDQL + ", 语法错误：请保证是 INSERT 和 UPDATE 连用！！！！");
                return 0;
            }
        } else {
            logger.error("BDQL语句：" + BDQL + "，语法不正确，请检查分号用法");
            return 0;
        }

    }

    /**
     * 开始解析BDQL不出意外的话这个函数是第一个使用
     *
     * @param sql
     * @return
     */
    public static ParserResult work(String sql) {
        ParserResult result = new ParserResult();
        int sort = getSort(lowercaseToUpperCase(sql));
        return BDQLParser.BDQLParser(sql, sort);
    }




}