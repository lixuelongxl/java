package com.dtstack.taier.scheduler.dto.schedule;

import java.util.List;

/**
 * @Auther: dazhi
 * @Date: 2021/12/6 3:42 PM
 * @Email:dazhi@dtstack.com
 * @Description:
 */
public class QueryTaskListDTO {

    /**
     * 租户
     */
    private Long tenantId;

    /**
     * 所属用户
     */
    private Long operatorId;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 调度状态：0 正常 1冻结 2停止
     */
    private Integer scheduleStatus;

    /**
     * 最近修改的开始时间
     */
    private Long startModifiedTime;

    /**
     * 最近修改的结束时间
     */
    private Long endModifiedTime;

    /**
     * 任务类型
     */
    private List<Integer> taskTypeList;

    /**
     * 周期类型
     */
    private List<Integer> periodTypeList;

    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 每页数
     */
    private Integer pageSize;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(Integer scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public Long getStartModifiedTime() {
        return startModifiedTime;
    }

    public void setStartModifiedTime(Long startModifiedTime) {
        this.startModifiedTime = startModifiedTime;
    }

    public Long getEndModifiedTime() {
        return endModifiedTime;
    }

    public void setEndModifiedTime(Long endModifiedTime) {
        this.endModifiedTime = endModifiedTime;
    }

    public List<Integer> getTaskTypeList() {
        return taskTypeList;
    }

    public void setTaskTypeList(List<Integer> taskTypeList) {
        this.taskTypeList = taskTypeList;
    }

    public List<Integer> getPeriodTypeList() {
        return periodTypeList;
    }

    public void setPeriodTypeList(List<Integer> periodTypeList) {
        this.periodTypeList = periodTypeList;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
