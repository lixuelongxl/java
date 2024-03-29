package com.dtstack.taier.develop.dto.devlop;
import java.util.HashMap;

public class SubJobVerticeDTO {


    private Integer parallelism;
    /**
     * 字任务id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 接收记录数
     */
    private Long recordsReceived;

    /**
     * 接收数量
     */
    private Long recordsSent;

    /**
     * 延迟
     */
    private Double delay;

    private HashMap<Integer,Long> recordsReceivedMap = new HashMap<>();

    private HashMap<Integer,Long> recordsSentMap = new HashMap<>();

    private HashMap<Integer, HashMap<String,Double>> delayMap = new HashMap<>();

    private HashMap<String,Double> delayMapList = new HashMap<>();


    public Integer getParallelism() {
        return parallelism;
    }

    public void setParallelism(Integer parallelism) {
        this.parallelism = parallelism;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRecordsReceived() {
        return recordsReceived;
    }

    public void setRecordsReceived(Long recordsReceived) {
        this.recordsReceived = recordsReceived;
    }

    public Long getRecordsSent() {
        return recordsSent;
    }

    public void setRecordsSent(Long recordsSent) {
        this.recordsSent = recordsSent;
    }

    public Double getDelay() {
        return delay;
    }

    public void setDelay(Double delay) {
        this.delay = delay;
    }

    public HashMap<Integer, Long> getRecordsReceivedMap() {
        return recordsReceivedMap;
    }

    public void setRecordsReceivedMap(HashMap<Integer, Long> recordsReceivedMap) {
        this.recordsReceivedMap = recordsReceivedMap;
    }

    public HashMap<Integer, Long> getRecordsSentMap() {
        return recordsSentMap;
    }

    public void setRecordsSentMap(HashMap<Integer, Long> recordsSentMap) {
        this.recordsSentMap = recordsSentMap;
    }

    public HashMap<Integer, HashMap<String, Double>> getDelayMap() {
        return delayMap;
    }

    public void setDelayMap(HashMap<Integer, HashMap<String, Double>> delayMap) {
        this.delayMap = delayMap;
    }

    public HashMap<String, Double> getDelayMapList() {
        return delayMapList;
    }

    public void setDelayMapList(HashMap<String, Double> delayMapList) {
        this.delayMapList = delayMapList;
    }
}
