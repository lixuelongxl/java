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

package com.dtstack.taier.develop.mapstruct.vo;

import com.dtstack.taier.develop.dto.devlop.ExecuteResultVO;
import com.dtstack.taier.develop.vo.develop.result.BatchExecuteResultVO;
import com.dtstack.taier.develop.vo.develop.result.BatchJobFindTaskRuleJobResultVO;
import com.dtstack.taier.scheduler.vo.ScheduleDetailsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BatchJobMapstructTransfer {
    BatchJobMapstructTransfer INSTANCE = Mappers.getMapper(BatchJobMapstructTransfer.class);


    /**
     * ExecuteResultVO --> BatchExecuteResultVO
     *
     * @param executeResultVO
     * @return
     */
    BatchExecuteResultVO executeResultVOToBatchExecuteResultVO(ExecuteResultVO executeResultVO);



    /**
     * ScheduleDetailsVO --> BatchJobFindTaskRuleJobResultVO
     * @param scheduleDetailsVO
     * @return
     */
    BatchJobFindTaskRuleJobResultVO scheduleDetailsVOToBatchJobFindTaskRuleJobResultVO(ScheduleDetailsVO scheduleDetailsVO);

}
