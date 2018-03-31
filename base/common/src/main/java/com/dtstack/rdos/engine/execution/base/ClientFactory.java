package com.dtstack.rdos.engine.execution.base;

import java.util.Map;

import com.dtstack.rdos.engine.execution.base.callback.ClassLoaderCallBack;
import com.dtstack.rdos.engine.execution.base.callback.ClassLoaderCallBackMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * TODO 修改---根据不同的配置生成不同的client实例
 * Date: 2017/2/20
 * Company: www.dtstack.com
 * @author xuchao
 */

public class ClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(ClientFactory.class);

    private static Map<String, ClassLoader> pluginClassLoader = Maps.newConcurrentMap();

    private static Map<String, String> typeRefClassName = Maps.newHashMap();

    static{
        typeRefClassName.put("flink120", "com.dtstack.rdos.engine.execution.flink120.FlinkClient");
        typeRefClassName.put("flink130", "com.dtstack.rdos.engine.execution.flink130.FlinkClient");
        typeRefClassName.put("flink140", "com.dtstack.rdos.engine.execution.flink140.FlinkClient");
        typeRefClassName.put("spark", "com.dtstack.rdos.engine.execution.spark210.SparkClient");
        typeRefClassName.put("datax", "com.dtstack.rdos.engine.execution.datax.DataxClient");
        typeRefClassName.put("spark_yarn", "com.dtstack.rdos.engine.execution.sparkyarn.SparkYarnClient");
        typeRefClassName.put("mysql", "com.dtstack.rdos.engine.execution.mysql.MysqlClient");
        typeRefClassName.put("oracle", "com.dtstack.rdos.engine.execution.oracle.OracleClient");
        typeRefClassName.put("sqlserver", "com.dtstack.rdos.engine.execution.sqlserver.SqlserverClient");
        typeRefClassName.put("maxcompute", "com.dtstack.rdos.engine.execution.odps.OdpsClient");
        typeRefClassName.put("hadoop", "com.dtstack.rdos.engine.execution.hadoop.HadoopClient");
    }

    public static ClassLoader getClassLoader(String pluginType){
        return pluginClassLoader.get(pluginType);
    }
    
	public static IClient createPluginClass(String pluginType) throws Exception{

        Map<String, IClient> clientMap = Maps.newConcurrentMap();
        ClassLoader classLoader = pluginClassLoader.get(pluginType);
        ClassLoaderCallBackMethod.callbackAndReset(new ClassLoaderCallBack<Object>(){

            @Override
            public Object execute() throws Exception {
                String className = typeRefClassName.get(pluginType);
                if(className == null){
                    throw new RuntimeException("not support for engine type " + pluginType);
                }

                IClient client = classLoader.loadClass(className).asSubclass(IClient.class).newInstance();
                ClientProxy proxyClient = new ClientProxy(client);
                clientMap.put(pluginType, proxyClient);
                return null;
            }
        }, classLoader, true);

        return clientMap.get(pluginType);
    }

    public static void addClassLoader(String pluginType, ClassLoader classLoader){
        if(pluginClassLoader.containsKey(pluginType)){
            return;
        }

        pluginClassLoader.putIfAbsent(pluginType, classLoader);
    }

    public static boolean checkContainClassLoader(String pluginType){
        return pluginClassLoader.containsKey(pluginType);
    }
}
