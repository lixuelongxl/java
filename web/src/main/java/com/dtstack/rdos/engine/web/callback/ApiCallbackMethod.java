package com.dtstack.rdos.engine.web.callback;


import com.dtstack.rdos.commom.exception.ErrorCode;
import com.dtstack.rdos.commom.exception.RdosException;
import io.vertx.ext.web.RoutingContext;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dtstack.rdos.engine.web.enums.Code;
import com.dtstack.rdos.engine.web.util.ResponseUtil;

/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年12月30日 下午1:16:37
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class ApiCallbackMethod {
	
	private final static Logger logger = LoggerFactory
			.getLogger(ApiCallbackMethod.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static void doCallback(ApiCallback ac, RoutingContext context) {
		ApiResult apiResult = new ApiResult();
		try {
			long start = System.currentTimeMillis();
			apiResult.setData(ac.execute());
			apiResult.setCode(ErrorCode.SUCCESS.getCode());
			long end = System.currentTimeMillis();
			apiResult.setSpace(end - start);
			ResponseUtil.res200(context, objectMapper.writeValueAsString(apiResult));
		} catch (Throwable e) {

            ErrorCode errorCode;
            String errorMsg;
            RdosException rdosDefineException = null;
			if (e.getCause() instanceof RdosException) {
				rdosDefineException = ((RdosException)e.getCause());
				errorCode = rdosDefineException.getErrorCode();
				errorMsg = rdosDefineException.getErrorMsg();
			} else if (e instanceof RdosException) {
				rdosDefineException = ((RdosException)e);
				errorCode = rdosDefineException.getErrorCode();
				errorMsg = rdosDefineException.getErrorMsg();
			}else{
				errorCode = ErrorCode.SERVER_EXCEPTION;
				errorMsg = ErrorCode.SERVER_EXCEPTION.getDescription();
			}
			apiResult.setCode(errorCode.getCode());
			String errMsg = e.getCause() != null ? e.getCause().toString() : e.getMessage();
			apiResult.setErrorMsg(errMsg);
			if(logger.isDebugEnabled()){
				logger.debug("ApiCallbackMethod error:", e);
			}
			try {
                ResponseUtil.res500(context, ApiResult.createErrorResultJsonStr(errorCode.getCode(), errorMsg));
			} catch (Throwable e1) {
				logger.error("ApiCallbackMethod error:", e1);
			}
		}
	}
}