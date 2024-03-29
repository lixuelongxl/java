package com.dtstack.taier.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Auther: dazhi
 * @Date: 2021/12/6 10:25 AM
 * @Email:dazhi@dtstack.com
 * @Description:
 */
@TableName(value = "schedule_job_expand")
public class ScheduleJobExpand implements Serializable {

    private static final long serialVersionUID = 228195023307246450L;

    /**
     *
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String jobId;

    /**
     * 任务提交额外信息
     */
    private String jobExtraInfo;

    /**
     * 引擎日志
     */
    private String engineLog;

    /**
     * 提交日志
     */
    private String logInfo;

    /**
     * 创建时间
     */
    private Timestamp gmtCreate;

    /**
     * 最近一次修改时间
     */
    private Timestamp gmtModified;

    /**
     * 是否逻辑删除
     */
    private Integer isDeleted;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobExtraInfo() {
        return jobExtraInfo;
    }

    public void setJobExtraInfo(String jobExtraInfo) {
        this.jobExtraInfo = jobExtraInfo;
    }

    public String getEngineLog() {
        return engineLog;
    }

    public void setEngineLog(String engineLog) {
        this.engineLog = engineLog;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
