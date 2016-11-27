## netty4示例

照着网上例子,及自己摸索做了一些示例，按照学习顺序,包括

- discard

    server端收到client 发送的消息后,啥都不干,直接扔掉

- echo

    client发送一个消息,server 回复一个消息,使用低级的ByteBuf api, 没有做任何的编解码器

- time

    server 在 client 连接上时发送一个UnixTime 对象,使用了定长的消息编码器,

    其中在server 需要对发出的信息做编码,所以添加一个TimeEncoder,

    并且不需要解码从client发送的消息,所以serverHandler 使用的是级别较低的ChannelInboundHandlerAdapter,

    client 需要天际一个TimeDecode解码器用于解码从server 端发送的UnixTime对象, clientHandler使用SimpleChannelInboundHandler

    在channelRead0 方法中能直接读取到UnixTime对象

- chat

    chat 需要双端通讯, 添加了String类型的编解码器,并且在入站时添加DelimiterBasedFrameDecoder作为解包的解码器,

    需要注意入站的顺序,是先使用DelimiterBasedFrameDecoder解决tcp的粘包,分包,再使用StringDecode完成对消息的解码。

    同时server client 的handler都使用了SimpleChannelInboundHandler,这样在读取消息的时候能直接读取StringDecode 解码后的String,

    在发送消息时,在消息的末尾添加'\r\n',DelimiterBasedFrameDecoder就是通过这个对消息做分割

- codec

    自定义消息编解码器,将消息分为消息头,消息体,被传输对象的对象需要实现Serializable接口

- heartbeat

    网上通常的做法是服务端在空闲的时候向客户端发送心跳包,客户度收到心跳包后回应一个消息,

    服务端收到消息后,记录该客户端在线,如果长时间没有客户端的心跳消息,则清除这个连接。

    把心跳任务放在服务端的好处是:客户端不是netty或其他语言做客户端时,可以有效减轻客户端的编程难度。

    实现client端的心跳连接,用一个定时任务自动清除没有发心跳包的client。步骤如下:

    - 客户端在空闲的时候(IdleStateHandler)向服务端发送心跳包
    - 服务端在每次与客户端通信时记录该客户端的最后访问时间
    - 服务端维护在线的客户端列表,定时扫描列表,清除长时间(自定义,如5分钟)未与服务端通讯的客户端



- reconnect

    客户端自动重连,

    - 在启动时没有连接服务器,启动定时任务重新连接
    - 在客户端丢弃连接时,启动定时任务重新连接


- httpserver

    实现一个httpserver, 浏览器访问`http://127.0.0.1:5000/?name=1`即可返回一串json信息

- rpc

    实现简单的rpc 服务, 没有做server 根据client传递过来的的接口,方法名,方法参数等信息查找实现方法,只是简单的返回


- push

    推送服务