/**
 * 1. 客户端在空闲的时候(IdleStateHandler)向服务端发送心跳包
 * 2. 服务端在每次与客户端通信时记录该客户端的最后访问时间
 * 2. 服务端维护在线的客户端列表,定时扫描列表,清除长时间(自定义,如5分钟)未与服务端通讯的客户端
 */
package com.zz.nettystudy.sample.heartbeat;