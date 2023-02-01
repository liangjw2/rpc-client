package com.ljw.client;

import com.ljw.client.config.ClientConfigOfLifeCycle;
import com.ljw.client.util.RpcClient;
import com.ljw.client.util.RpcProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Initialize {

    private static final Logger logger = LoggerFactory.getLogger(Initialize.class);

    public static void initClient(){

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientConfigOfLifeCycle.class);
        logger.info("初始化rpc客户端applicationContext……");
        RpcClient rpcClient = applicationContext.getBean(RpcClient.class);
        RpcProxy rpcProxy = applicationContext.getBean(RpcProxy.class);
        logger.info("bean1:rpcClient{}",rpcClient);
        logger.info("bean2:rpcProxy{}",rpcProxy);

    }

}
