package com.ljw.client.util;

import com.ljw.client.vo.RpcRequest;
import com.ljw.client.vo.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.SynchronousQueue;

public class RpcClientInboundHandler extends ChannelInboundHandlerAdapter {
    private Map<String, SynchronousQueue<RpcResponse>> results;
    private  final Logger logger = LoggerFactory.getLogger(RpcClientInboundHandler.class);

    public RpcClientInboundHandler(Map<String, SynchronousQueue<RpcResponse>> results){
        this.results = results;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = (RpcResponse) msg;
        logger.info("收到服务器响应:{}", rpcResponse);
        if(!rpcResponse.isSuccess()){
            throw new RuntimeException("调用结果异常,异常信息:" + rpcResponse.getErrorMessage());
        }
        // 取出结果容器,将response放进queue中
        SynchronousQueue<RpcResponse> queue = results.get(rpcResponse.getRequestId());
        queue.put(rpcResponse);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleState.ALL_IDLE){
                logger.info("发送心跳包");
                RpcRequest request = new RpcRequest();
                request.setMethodName("heartBeat");
                ctx.channel().writeAndFlush(request);
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        logger.info("异常:{}", cause.getMessage());
        ctx.channel().close();
    }
}
