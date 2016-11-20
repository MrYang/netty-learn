package com.zz.nettystudy.sample.rpc.server;

import com.zz.nettystudy.sample.rpc.HelloService;
import com.zz.nettystudy.sample.rpc.HelloServiceImpl;
import com.zz.nettystudy.sample.rpc.RpcRequest;
import com.zz.nettystudy.sample.rpc.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(msg.getRequestId());
        String interfaceName = msg.getInterfaceName();
        String methodName = msg.getMethodName();
        Class<?>[] parameterTypes = msg.getParameterTypes();
        Object[] parameters = msg.getParameters();
        logger.info("interfaceName:{},methodName:{},parameterTypes:{},parameters:{}",
                interfaceName, methodName, parameterTypes, parameters);

        // 根据传递过来的参数寻找实现方法
        HelloService helloService = new HelloServiceImpl();
        String result = helloService.hello("Mr.Yang");
        rpcResponse.setStatusCode(0);
        rpcResponse.setResult(result);
        ctx.writeAndFlush(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
