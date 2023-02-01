package com.ljw.client.util;

import com.ljw.client.util.json.JsonDecoder;
import com.ljw.client.util.json.JsonEncoder;
import com.ljw.client.vo.RpcRequest;
import com.ljw.client.vo.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

/**
 * RPC远程调用的客户端
 */
@Component
public class RpcClient implements ApplicationContextAware, InitializingBean {

    //@Value("${rpc.remote.ip}")
    private String remoteIp="localhost";

    //@Value("${rpc.remote.port}")
    private int port=9998;

    private Bootstrap bootstrap;
    private final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    // 储存调用结果
    private final Map<String, SynchronousQueue<RpcResponse>> results = new ConcurrentHashMap<>();

    //public RpcClient(){

    //}

    //@PostConstruct

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //logger.info("RPC 服务端口est1:" + port);
    }

    @Override
    public void afterPropertiesSet() {
        start();
    }
    public void start(){
        bootstrap = new Bootstrap().remoteAddress(remoteIp, port);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 0, 10));
                        pipeline.addLast(new JsonEncoder());
                        pipeline.addLast(new JsonDecoder());
                        pipeline.addLast(new RpcClientInboundHandler(results));
                    }
                });
        //logger.info("RPC 服务端口test2:" + port);
    }

    public RpcResponse send(RpcRequest rpcRequest) {
        RpcResponse rpcResponse = null;
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        Channel channel = null;
        try {
            channel = bootstrap.connect().sync().channel();
            logger.info("连接建立, 发送请求:{}", rpcRequest);
            channel.writeAndFlush(rpcRequest);
            SynchronousQueue<RpcResponse> queue = new SynchronousQueue<>();
            results.put(rpcRequest.getRequestId(), queue);
            // 阻塞等待获取响应
            rpcResponse = queue.take();
            results.remove(rpcRequest.getRequestId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(channel != null && channel.isActive()){
                channel.close();
            }
        }
        return rpcResponse;
    }
}
