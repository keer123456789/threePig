package com.zrsy.threepig.domain.BDQL;

import java.util.Map;

/**
 * 本类是BDQL服务的。
 * 用于BDQL执行insert、update操作是存储数据
 *
 * tableName是数据insert、update的表名
 * tableData 是数据内容，以Map为结构存储
 */
public class BigchainDBData {
    //表名
    private String tableName;
    //表中数据
    private Map tableData;

    public BigchainDBData() {
    }

    public BigchainDBData(String tableName, Map tableData) {
        this.tableName = tableName;
        this.tableData = tableData;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map getTableData() {
        return tableData;
    }

    public void setTableData(Map tableData) {
        this.tableData = tableData;
    }
}