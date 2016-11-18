/**
 * ChannelPipeline 类doc写的很清楚
 * ChannelInboundHandler按照注册的先后顺序执行,代表从socket网络层发出的事件,典型的处理器如Decoder,将tcp字节流解码为应用层关心的协议报文对象
 * ChannelOutboundHandler按照注册的先后顺序逆序执行,代表从应用层发出的事件,典型的处理器如Encoder,将协议报文对象编码为字节流，供底层网络发送
 * request->InboundHandler1->InboundHandler2->OutboundHandler2->OutboundHandler1->response
 *
 * p.addLast("1", new InboundHandlerA());
 * p.addLast("2", new InboundHandlerB());
 * p.addLast("3", new OutboundHandlerA());
 * p.addLast("4", new OutboundHandlerB());
 * p.addLast("5", new InboundOutboundHandlerX());
 *
 * 对于入站事件，处理序列为：1->2->5；对于出站事件，处理序列为：5->4->3
 *
 * ChannelPipeline中流动的是事件(事件中可能附加数据)
 * 事件在ChannelPipeline中不自动流动而需要调用ChannelHandlerContext中诸如fileXXX()或者read()类似的方法将事件从一个ChannelHandler传播到下一个ChannelHandler
 * ChannelInboundHandlerAdapter,ChannelOutboundHandlerAdapter 中事件默认自动传播到下一个Handler
 *
 *
 * 入站事件一般由I/O线程触发，以下事件为入站事件
 * ChannelRegistered() // Channel注册到EventLoop
 * ChannelActive()     // Channel激活
 * ChannelRead(Object) // Channel读取到数据
 * ChannelReadComplete()   // Channel读取数据完毕
 * ExceptionCaught(Throwable)  // 捕获到异常
 * UserEventTriggered(Object)  // 用户自定义事件
 * ChannelWritabilityChanged() // Channnel可写性改变，由写高低水位控制
 * ChannelInactive()   // Channel不再激活
 * ChannelUnregistered()   // Channel从EventLoop中注销
 *
 * 出站事件一般由用户触发，以下事件为出站事件：
 * bind(SocketAddress, ChannelPromise) // 绑定到本地地址
 * connect(SocketAddress, SocketAddress, ChannelPromise)   // 连接一个远端机器
 * write(Object, ChannelPromise)   // 写数据，实际只加到Netty出站缓冲区
 * flush() // flush数据，实际执行底层写
 * read()  // 读数据，实际设置关心OP_READ事件，当数据到来时触发ChannelRead入站事件
 * disconnect(ChannelPromise)  // 断开连接，NIO Server和Client不支持，实际调用close
 * close(ChannelPromise)   // 关闭Channel
 * deregister(ChannelPromise)  // 从EventLoop注销Channel
 *
 */

package com.zz.nettystudy;