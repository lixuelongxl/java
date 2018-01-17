package com.dtstack.rdos.engine.entrance.service;

import java.util.Map;

import com.dtstack.rdos.commom.exception.RdosException;
import com.dtstack.rdos.common.annotation.Forbidden;
import com.dtstack.rdos.common.util.MathUtil;
import com.dtstack.rdos.common.util.PublicUtil;
import com.dtstack.rdos.engine.db.dao.RdosEngineBatchJobDAO;
import com.dtstack.rdos.engine.db.dao.RdosEngineJobCacheDao;
import com.dtstack.rdos.engine.db.dao.RdosEngineStreamJobDAO;
import com.dtstack.rdos.engine.db.dataobject.RdosEngineBatchJob;
import com.dtstack.rdos.engine.db.dataobject.RdosEngineStreamJob;
import com.dtstack.rdos.engine.entrance.enumeration.RequestStart;
import com.dtstack.rdos.engine.entrance.node.MasterNode;
import com.dtstack.rdos.engine.execution.base.JobSubmitExecutor;
import com.dtstack.rdos.engine.execution.base.enumeration.ComputeType;
import com.dtstack.rdos.engine.execution.base.enumeration.EJobCacheStage;
import com.dtstack.rdos.engine.execution.base.enumeration.RdosTaskStatus;
import com.dtstack.rdos.engine.execution.base.pojo.ParamAction;
import com.dtstack.rdos.engine.entrance.zk.ZkDistributed;
import com.dtstack.rdos.engine.entrance.zk.data.BrokerDataNode;
import com.dtstack.rdos.engine.execution.base.JobClient;
import com.dtstack.rdos.engine.execution.queue.ExeQueueMgr;
import com.dtstack.rdos.engine.send.HttpSendClient;
import com.dtstack.rdos.engine.util.TaskIdUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dtstack.rdos.engine.execution.base.JobClientCallBack;

/**
 * Reason: TODO ADD REASON(可选)
 * Date: 2017年03月03日 下午1:25:18
 * Company: www.dtstack.com
 *
 * @author sishu.yss
 */
public class ActionServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(ActionServiceImpl.class);

    private ZkDistributed zkDistributed = ZkDistributed.getZkDistributed();

    private RdosEngineStreamJobDAO streamTaskDAO = new RdosEngineStreamJobDAO();
    
    private RdosEngineBatchJobDAO batchJobDAO = new RdosEngineBatchJobDAO();
    
    private RdosEngineJobCacheDao engineJobCacheDao = new RdosEngineJobCacheDao();

    private MasterNode masterNode = MasterNode.getInstance();

    /**
     * TODO 拆分成两个方法,注意做判断,防止由于网络问题导致出现提交多次的问题
     * 1: 获取从web端发送过来的任务，然后转发到master节点
     * 2: 获取从master下发的任务, 并返回json:{"send":true/false}
     * @param params
     * @throws Exception
     */
    /*
    public void start(Map<String, Object> params) throws Exception {
        String ajobId = null;
        Integer acomputeType  = null;
        try {
            ParamAction paramAction = PublicUtil.mapToObject(params, ParamAction.class);
            checkParam(paramAction);
            if(receiveJob(paramAction)){
                String jobId = paramAction.getTaskId();
                ajobId = jobId;
                Integer computeType  = paramAction.getComputeType();
                acomputeType = computeType;
                String zkTaskId = TaskIdUtil.getZkTaskId(paramAction.getComputeType(), paramAction.getEngineType(), paramAction.getTaskId());
                boolean isAlreadyInThisNode = zkDistributed.checkIsAlreadyInThisNode(zkTaskId);
                String address = zkDistributed.getExecutionNode();

                updateJobZookStatus(zkTaskId,RdosTaskStatus.SUBMITTING.getStatus());
                updateJobStatus(jobId, computeType, RdosTaskStatus.SUBMITTING.getStatus());

                if (isAlreadyInThisNode || paramAction.getRequestStart() == RequestStart.NODE.getStart() || zkDistributed.getLocalAddress().equals(address)) {
              	    JobClient jobClient = new JobClient(paramAction);
                    jobClient.setJobClientCallBack(new JobClientCallBack() {

                        @Override
                        public void execute(Map<String, ? extends Object> params) {

                            if(!params.containsKey(JOB_STATUS)){
                                return;
                            }

                            int jobStatus = MathUtil.getIntegerVal(params.get(JOB_STATUS));
                            updateJobZookStatus(zkTaskId, jobStatus);
                            updateJobStatus(jobId, computeType, jobStatus);
                        }
                    });
                    addJobCache(jobId, paramAction.toString());
                    updateJobZookStatus(zkTaskId,RdosTaskStatus.WAITENGINE.getStatus());
                    updateJobStatus(jobId, computeType, RdosTaskStatus.WAITENGINE.getStatus());
                    jobClient.submitJob();
                } else {
                    paramAction.setRequestStart(RequestStart.NODE.getStart());
                    HttpSendClient.actionStart(address, paramAction);
                }
            }
        } catch (Throwable e) {
            //提交失败,修改对应的提交jobid为提交失败
            logger.error("", e);
            if (ajobId != null) {
                updateJobStatus(ajobId, acomputeType, RdosTaskStatus.FAILED.getStatus());
            }
        }
    }
    */

    /**
     * 接受来自客户端的请求, 目的是在master节点上组织成一个优先级队列
     * TODO 1：处理 重复发送的问题 2：rdos-web端的发送需要修改为向master节点发送--避免转发
     * @param params
     */
    public void start(Map<String, Object> params){

        try{
            ParamAction paramAction = PublicUtil.mapToObject(params, ParamAction.class);

            checkParam(paramAction);

            //判断localAddr == masterAddr ?
            if(zkDistributed.localIsMaster()){

                if(!receiveJob(paramAction)){
                    return;
                }

                //1: 直接提交到本地master的优先级队列
                JobClient jobClient = new JobClient(paramAction);
                masterNode.addTask(jobClient);
                return;
            }

            //2: 发送到master节点
            // ----获取master地址
            String masterAddr = zkDistributed.isHaveMaster();

            if(masterAddr == null){
                //TODO 如果遇到master 地址为null 应该如果处理
                return;
            }

            //---提交任务
            paramAction.setRequestStart(RequestStart.NODE.getStart());
            HttpSendClient.actionStart(masterAddr, paramAction);
        }catch (Exception e){
            logger.info("", e);
        }

    }

    /**
     * 执行从master上下发的任务
     * 不需要判断等待队列是否满了，由master节点主动判断
     * TODO 处理重复发送的问题
     * @param params
     * @return
     */
    public Map<String, Object> submit(Map<String, Object> params){
        Map<String, Object> result = Maps.newHashMap();
        String jobId = null;
        Integer computeType = null;

        try{

            ParamAction paramAction = PublicUtil.mapToObject(params, ParamAction.class);
            checkParam(paramAction);
            if(checkSubmitted(paramAction)){
                result.put("send", true);
                return result;
            }

            jobId = paramAction.getTaskId();
            computeType = paramAction.getComputeType();

            String zkTaskId = TaskIdUtil.getZkTaskId(paramAction.getComputeType(), paramAction.getEngineType(), paramAction.getTaskId());
            updateJobZookStatus(zkTaskId, RdosTaskStatus.ENGINEDISTRIBUTE.getStatus());
            updateJobStatus(jobId, computeType, RdosTaskStatus.ENGINEDISTRIBUTE.getStatus());

            JobClient jobClient = new JobClient(paramAction);
            String finalJobId = jobId;
            Integer finalComputeType = computeType;
            jobClient.setJobClientCallBack(new JobClientCallBack() {

                @Override
                public void execute(Map<String, ? extends Object> params) {

                    if(!params.containsKey(JOB_STATUS)){
                        return;
                    }

                    int jobStatus = MathUtil.getIntegerVal(params.get(JOB_STATUS));
                    updateJobZookStatus(zkTaskId, jobStatus);
                    updateJobStatus(finalJobId, finalComputeType, jobStatus);
                }
            });

            addJobCache(jobId, paramAction.getEngineType(), computeType, EJobCacheStage.IN_SUBMIT_QUEUE.getStage(), paramAction.toString());
            updateJobZookStatus(zkTaskId,RdosTaskStatus.WAITENGINE.getStatus());
            updateJobStatus(jobId, computeType, RdosTaskStatus.WAITENGINE.getStatus());
            jobClient.submitJob();

            result.put("send", true);
            return result;

        }catch (Exception e){
            //提交失败,修改对应的提交jobid为提交失败
            logger.error("", e);
            if (jobId != null) {
                updateJobStatus(jobId, computeType, RdosTaskStatus.FAILED.getStatus());
            }

            //也是处理成功的一种
            result.put("send", true);
            return result;
        }
    }

    /**
     * 检查是否可以下发任务
     * @param params
     */
    public Map<String, Object> checkCanDistribute(Map<String, Object> params){
        Map<String, Object> resultMap = Maps.newHashMap();
        String groupName = MathUtil.getString(params.get("groupName"));
        String engineType = MathUtil.getString(params.get("engineType"));

        Boolean canAdd = ExeQueueMgr.getInstance().checkCanAddToWaitQueue(engineType, groupName);
        resultMap.put("result", canAdd);
        return resultMap;
    }

    @Forbidden
    public void updateJobZookStatus(String taskId, Integer status){
        BrokerDataNode brokerDataNode = BrokerDataNode.initBrokerDataNode();
        brokerDataNode.getMetas().put(taskId, status.byteValue());
        zkDistributed.updateSynchronizedBrokerData(zkDistributed.getLocalAddress(), brokerDataNode, false);
        zkDistributed.updateLocalMemTaskStatus(brokerDataNode);

    }

    /**
     * TODO 判断任务是否是否是在本台机器上
     * @param params
     * @throws Exception
     */
    public void stop(Map<String, Object> params) throws Exception {
        ParamAction paramAction = PublicUtil.mapToObject(params, ParamAction.class);
        checkParam(paramAction);
        String zkTaskId = TaskIdUtil.getZkTaskId(paramAction.getComputeType(), paramAction.getEngineType(), paramAction.getTaskId());
        String jobId = paramAction.getTaskId();
        Integer computeType  = paramAction.getComputeType();
        JobClient jobClient = new JobClient(paramAction);
        jobClient.setJobClientCallBack(new JobClientCallBack(){

			@Override
			public void execute(Map<String, ? extends Object> exeParams) {

                if(!exeParams.containsKey(JOB_STATUS)){
                    return;
                }

                int jobStatus = MathUtil.getIntegerVal(exeParams.get(JOB_STATUS));

                updateJobZookStatus(zkTaskId, jobStatus);
                updateJobStatus(jobId, computeType, jobStatus);
                deleteJobCache(jobId);
			}
        	
        });
        jobClient.stopJob();
    }


    private void checkParam(ParamAction paramAction) throws Exception{

        if(StringUtils.isBlank(paramAction.getTaskId())){
           throw new RdosException("taskId is not allow null");
        }

        if(paramAction.getComputeType()==null){
            throw new RdosException("computeType is not allow null");
        }

        if(paramAction.getEngineType() == null){
            throw new RdosException("engineType is not allow null");
        }
    }

    private boolean checkSubmitted(ParamAction paramAction){
        boolean result;
        String jobId = paramAction.getTaskId();
        Integer computerType = paramAction.getComputeType();
        if (ComputeType.STREAM.getType().equals(computerType)) {
            RdosEngineStreamJob rdosEngineStreamJob = streamTaskDAO.getRdosTaskByTaskId(jobId);
            if(rdosEngineStreamJob == null){
                logger.error("can't find job from engineStreamJob:" + paramAction);
                return false;
            }

            result = RdosTaskStatus.canSubmitAgain(rdosEngineStreamJob.getStatus());
        }else{
            RdosEngineBatchJob rdosEngineBatchJob = batchJobDAO.getRdosTaskByTaskId(jobId);
            if(rdosEngineBatchJob == null){
                logger.error("can't find job from engineBatchJob:" + paramAction);
                return false;
            }

            result = RdosTaskStatus.canSubmitAgain(rdosEngineBatchJob.getStatus());

        }

        return result;
    }

    /**
     * master节点接收到任务，修改任务状态
     * 同时处理重复提交的问题
     * @param paramAction
     * @return
     */
    private boolean receiveJob(ParamAction paramAction){
    	boolean result;
    	String jobId = paramAction.getTaskId();
    	Integer computerType = paramAction.getComputeType();
        if (ComputeType.STREAM.getType().equals(computerType)) {
        	RdosEngineStreamJob rdosEngineStreamJob = streamTaskDAO.getRdosTaskByTaskId(jobId);
        	if(rdosEngineStreamJob == null){
        		rdosEngineStreamJob = new RdosEngineStreamJob();
        		rdosEngineStreamJob.setTaskId(jobId);
        		rdosEngineStreamJob.setStatus(RdosTaskStatus.UNSUBMIT.getStatus().byteValue());
        		streamTaskDAO.insert(rdosEngineStreamJob);
        		result =  true;
        	}else{
        		result = RdosTaskStatus.canStartAgain(rdosEngineStreamJob.getStatus());
        		if(result){
        			streamTaskDAO.updateTaskStatus(rdosEngineStreamJob.getTaskId(), RdosTaskStatus.ENGINEACCEPTED.getStatus().byteValue());
        		}
        	}
        }else{
        	RdosEngineBatchJob rdosEngineBatchJob = batchJobDAO.getRdosTaskByTaskId(jobId);
        	if(rdosEngineBatchJob == null){
        		rdosEngineBatchJob = new RdosEngineBatchJob();
        		rdosEngineBatchJob.setJobId(jobId);
        		rdosEngineBatchJob.setStatus(RdosTaskStatus.UNSUBMIT.getStatus().byteValue());
        		batchJobDAO.insert(rdosEngineBatchJob);
        		result =  true;
        	}else{
        		result = RdosTaskStatus.canStartAgain(rdosEngineBatchJob.getStatus());
        		if(result){
        			batchJobDAO.updateJobStatus(rdosEngineBatchJob.getJobId(), RdosTaskStatus.ENGINEACCEPTED.getStatus().byteValue());
        		}
        	}
        }
        return result;
    }
    
    @Forbidden
    public void updateJobStatus(String jobId, Integer computeType, Integer status) {
        if (ComputeType.STREAM.getType().equals(computeType)) {
            streamTaskDAO.updateTaskStatus(jobId, status);
        } else {
            batchJobDAO.updateJobStatus(jobId, status);
        }
    }

    @Forbidden
    public void addJobCache(String jobId, String engineType, Integer computeType, int stage, String jobInfo){
        engineJobCacheDao.insertJob(jobId, engineType, computeType, stage, jobInfo);
    }

    public void deleteJobCache(String jobId){
        engineJobCacheDao.deleteJob(jobId);
    }
}
