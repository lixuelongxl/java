package com.dtstack.taier.develop.service.template.oracle;

import com.alibaba.fastjson.JSONObject;
import com.dtstack.taier.develop.common.template.Reader;
import com.dtstack.taier.develop.service.template.BaseReaderPlugin;
import com.dtstack.taier.develop.service.template.PluginName;

import java.util.List;

/**
 * Date: 2020/1/7
 * Company: www.dtstack.com
 *
 * @author xiaochen
 */
public class OracleBinLogReader extends BaseReaderPlugin implements Reader {
    private String readPosition;
    private String startSCN;
    private List<String> table;
    private String cat;
    private Boolean pavingData;
    private String schema;
    private String jdbcUrl;
    private String username;
    private String password;
    // oracle 可拔插数据库（PDB）
    private String pdbName;

    public String getReadPosition() {
        return readPosition;
    }

    public void setReadPosition(String readPosition) {
        this.readPosition = readPosition;
    }

    public String getStartSCN() {
        return startSCN;
    }

    public void setStartSCN(String startSCN) {
        this.startSCN = startSCN;
    }

    public List<String> getTable() {
        return table;
    }

    public void setTable(List<String> table) {
        this.table = table;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Boolean getPavingData() {
        return pavingData;
    }

    public void setPavingData(Boolean pavingData) {
        this.pavingData = pavingData;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPdbName() {
        return pdbName;
    }

    public void setPdbName(String pdbName) {
        this.pdbName = pdbName;
    }

//    @Override
//    public JSONObject toReaderJson() {
//        JSONObject param = JSON.parseObject(JSON.toJSONString(this));
//        dealExtralConfig(param);
//        JSONObject res = new JSONObject();
//        res.put("name", PluginName.ORACLE_BINLOG_R);
//        res.put("type", DataSourceType.Oracle.getVal());
//        res.put("parameter", param);
//        return res;
//    }

    @Override
    public String toReaderJsonString() {
        return toReaderJson().toJSONString();
    }

    @Override
    public String pluginName() {
        return PluginName.ORACLE_BINLOG_R;
    }

    @Override
    public void checkFormat(JSONObject data) {

    }
}
