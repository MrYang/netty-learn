/**
 * 对于tcp的分包,拆包,解包有三种解决方案
 * 1. 消息定长, 对应time示例,写入接收的消息定长度为4个字节
 * 2. 在包尾增加一个标识，通过这个标志符进行分割, 对应chat 示例, 写入接收的消息在包尾添加'\r\n'字符,使用netty 自带的
 * DelimiterBasedFrameDecoder,StringDecoder,StringEncoder 解码器
 * 3. 将消息分为两部分，也就是消息头和消息体，消息头中写入要发送数据的总长度，
 * 通常是在消息头的第一个字段使用int值来标识发送数据的长度(对应codec 示例)
 */

package com.zz.nettystudy.sample.codec;