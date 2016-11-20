package com.zz.nettystudy.sample.rpc.client;

import com.zz.nettystudy.sample.codec.CodecDecoder;
import com.zz.nettystudy.sample.codec.CodecEncoder;
import com.zz.nettystudy.sample.rpc.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RpcClient {

    private static Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private static LinkedBlockingQueue<RpcRequest> requestQueue = new LinkedBlockingQueue<>();

    private static boolean close = false;

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 5000;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new CodecDecoder());
                    ch.pipeline().addLast(new CodecEncoder());
                    ch.pipeline().addLast(new RpcClientHandler());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            f.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("client connected success");
                        f.channel().eventLoop().scheduleAtFixedRate(() -> {
                            RpcRequest rpcRequest = new RpcRequest();
                            rpcRequest.setRequestId(UUID.randomUUID().toString());
                            rpcRequest.setInterfaceName("helloService");
                            rpcRequest.setMethodName("hello");
                            rpcRequest.setParameterTypes(new Class[]{String.class});
                            rpcRequest.setParameters(new Object[]{"parameter"});
                            try {
                                requestQueue.put(rpcRequest);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }, 0, 1, TimeUnit.SECONDS);
                    } else {
                        logger.info("client connected failed");
                        future.cause().printStackTrace();
                    }
                }
            });

            while (true) {
                if (close) {
                    break;
                }

                RpcRequest rpcRequest = requestQueue.take();
                Instant start = Instant.now();
                f.channel().writeAndFlush(rpcRequest);
                logger.info("rpc consume time:{}", Duration.between(start,Instant.now()).toMillis());
            }

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
