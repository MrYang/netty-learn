/**
 * 1. 启动client,查看日志可观察到不停重试连接server
 * 2. 启动server,查看日志可观察到连接上server
 * 3. 关闭server,触发client inactive事件,再次调度connect方法,不断重试连接
 * 4. 启动server,查看日志可观察到连接上server
 */

package com.zz.nettystudy.sample.reconnect;