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

package com.dtstack.taier.develop.service.develop.impl;


import com.dtstack.taier.develop.utils.develop.common.IDownload;

import java.util.ArrayList;
import java.util.List;

public class SyncDownload implements IDownload {
    @Override
    public void configure() {

    }

    @Override
    public List<String> getMetaInfo() {
        return new ArrayList<>();
    }

    @Override
    public Object readNext() {
        return null;
    }

    @Override
    public boolean reachedEnd() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public String getFileName() {
        return null;
    }

    private String logInfo;


    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }
}
