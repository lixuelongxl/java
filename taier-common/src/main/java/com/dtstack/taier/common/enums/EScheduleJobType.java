/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.taier.common.enums;

import com.dtstack.taier.common.exception.RdosDefineException;

import java.util.ArrayList;
import java.util.List;

public enum EScheduleJobType {

    /**
     * 虚节点
     */
    VIRTUAL(-1, "虚节点", -1, 0, null, EComputeType.BATCH),

    /**
     * SparkSQL
     */
    SPARK_SQL(0, "SparkSQL", 0, 1, EComponentType.SPARK, EComputeType.BATCH),

    /**
     * Spark
     */
    SPARK(1, "Spark", 1, 2, EComponentType.SPARK, EComputeType.BATCH),

    /**
     * 数据同步
     */
    SYNC(2, "数据同步", 2, 3, EComponentType.FLINK, EComputeType.BATCH),
    /**
     * Shell
     */
    SHELL(3, "Shell", 2, 3, null, EComputeType.BATCH),

    /**
     * FlinkSQL
     */
    SQL(5, "FlinkSQL", 0, 5, EComponentType.FLINK, EComputeType.STREAM),

    DATA_ACQUISITION(6, "实时采集", 2, 4,EComponentType.FLINK, EComputeType.STREAM),

    HIVE_SQL(7, "HiveSQL", 0, 4,EComponentType.HIVE_SERVER, EComputeType.BATCH),
    /**
     * 工作流
     */
    WORK_FLOW(10, "工作流", -1, 9, null, EComputeType.BATCH),
    ;

    private Integer type;

    private String name;

    /**
     * 引擎能够接受的jobType
     * SQL              0
     * MR               1
     * SYNC             2
     * PYTHON           3
     * 不接受的任务类型    -1
     */
    private Integer engineJobType;

    private Integer sort;

    private EComponentType componentType;

    /**
     * 任务所属类型
     */
    private EComputeType computeType;


    public static final List<Integer> STREAM_JOB_TYPES = new ArrayList<>();
    public static final List<Integer> BATCH_JOB_TYPES = new ArrayList<>();
    static {
        for (EScheduleJobType value : EScheduleJobType.values()) {
            if (EComputeType.STREAM == value.getComputeType()){
                STREAM_JOB_TYPES.add(value.getValue());
            }
            if (EComputeType.BATCH == value.getComputeType()){
                BATCH_JOB_TYPES.add(value.getValue());
            }
        }
    }

    EScheduleJobType(Integer type, String name, Integer engineJobType, Integer sort, EComponentType componentType, EComputeType computeType) {
        this.type = type;
        this.name = name;
        this.engineJobType = engineJobType;
        this.sort = sort;
        this.componentType = componentType;
        this.computeType = computeType;
    }

    public static EScheduleJobType getByTaskType(int type) {
        EScheduleJobType[] eJobTypes = EScheduleJobType.values();
        for (EScheduleJobType eJobType : eJobTypes) {
            if (eJobType.type == type) {
                if (eJobType.getValue() != -1) {
                    return eJobType;
                }
                break;
            }
        }
        throw new RdosDefineException("不支持的任务类型");
    }

    public Integer getValue() {
        return type;
    }

    public Integer getVal() {
        return type;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Integer getEngineJobType() {
        return engineJobType;
    }

    public Integer getSort() {
        return sort;
    }

    public EComputeType getComputeType() {
        return computeType;
    }

    public EComponentType getComponentType() {
        return componentType;
    }

}