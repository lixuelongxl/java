package com.dtstack.taier.develop.service.template.rdbms;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dtstack.dtcenter.loader.source.DataSourceType;
import com.dtstack.taier.common.exception.RdosDefineException;
import com.dtstack.taier.develop.service.template.BaseWriterPlugin;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * RDB字段
 * @author ：daojin
 * date：Created in 上午10:40 2021/7/7
 */
public abstract class RDBWriter extends BaseWriterPlugin {

    protected List connection;
    protected String jdbcUrl;
    protected List<String> table;
    protected String schema;

    protected String password;
    protected String username;
    protected List column;
    protected List fullcolumn;
    protected List preSql;
    protected List postSql;
    protected String writeMode;
    protected Integer batchSize;
    protected Map<String, List> updateKey;
    protected List<Long> sourceIds;
    protected DataSourceType type;

    public List getConnection() {
        return connection;
    }

    public void setConnection(List connection) {
        this.connection = connection;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public List<String> getTable() {
        return table;
    }

    public void setTable(List<String> table) {
        this.table = table;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List getColumn() {
        return column;
    }

    public void setColumn(List column) {
        this.column = column;
    }

    public List getFullcolumn() {
        return fullcolumn;
    }

    public void setFullcolumn(List fullcolumn) {
        this.fullcolumn = fullcolumn;
    }

    public List getPreSql() {
        return preSql;
    }

    public void setPreSql(List preSql) {
        this.preSql = preSql;
    }

    public List getPostSql() {
        return postSql;
    }

    public void setPostSql(List postSql) {
        this.postSql = postSql;
    }

    public String getWriteMode() {
        return writeMode;
    }

    public void setWriteMode(String writeMode) {
        this.writeMode = writeMode;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Map<String, List> getUpdateKey() {
        return updateKey;
    }

    public void setUpdateKey(Map<String, List> updateKey) {
        this.updateKey = updateKey;
    }

    @Override
    public List<Long> getSourceIds() {
        return sourceIds;
    }

    @Override
    public void setSourceIds(List<Long> sourceIds) {
        this.sourceIds = sourceIds;
    }

    public DataSourceType getType() {
        return type;
    }

    public void setType(DataSourceType type) {
        this.type = type;
    }

    @Override
    public void checkFormat(JSONObject data) {
        data = data.getJSONObject("parameter");

        if (data.get("column") == null){
            throw new RdosDefineException("column 不能为空，需要匹配映射");
        }
        if (!(data.get("column") instanceof JSONArray)) {
            throw new RdosDefineException("column 必须为数组格式");
        }

        JSONArray column = data.getJSONArray("column");
        if (column.isEmpty()) {
            throw new RdosDefineException("需要匹配映射");
        }

        if (data.get("connection") == null) {
            throw new RdosDefineException("connection 不能为空");
        }

        if (!(data.get("connection") instanceof JSONArray)) {
            throw new RdosDefineException("connection 必须为数组格式");
        }

        JSONArray connections = data.getJSONArray("connection");
        if (connections.isEmpty()) {
            throw new RdosDefineException("connection 不能为空");
        }

        if (connections.size() > 1) {
            throw new RdosDefineException("暂不支持多个数据源写入");
        }

        if (StringUtils.isEmpty(connections.getJSONObject(0).getString("jdbcUrl"))) {
            throw new RdosDefineException("jdbcUrl 不能为空");
        }

        if (connections.getJSONObject(0).get("table") == null) {
            throw new RdosDefineException("table 不能为空");
        }

        if (!(connections.getJSONObject(0).get("table") instanceof JSONArray)) {
            throw new RdosDefineException("table 必须为数组格式");
        }

        JSONArray tables = connections.getJSONObject(0).getJSONArray("table");
        if (tables.isEmpty()) {
            throw new RdosDefineException("table 不能为空");
        }

        if (tables.size() > 1) {
            throw new RdosDefineException("暂不支持多张表写入");
        }

        for (Object table : tables) {
            if (!(table instanceof String)) {
                throw new RdosDefineException("table 必须为字符串数组格式");
            }
        }
    }

}
