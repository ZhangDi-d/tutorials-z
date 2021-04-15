package org.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.log4j.Log4j2;
import org.example.server.handler.NettyServerHandlerInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @author dizhang
 * @date 2021-04-15
 */
@Log4j2
@Component
public class NettyServer {

    @Value("${netty.port}")
    private Integer port;

    @Autowired
    private NettyServerHandlerInitializer nettyServerHandlerInitializer;

    /**
     * boss 线程组，用于服务端接受客户端的连接
     */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    /**
     * worker 线程组，用于服务端接受客户端的数据读写
     */
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    /**
     * Netty Server Channel
     */
    private Channel channel;


    /**
     * 启动 Netty Server
     */
    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)  // 指定 Channel 为服务端 NioServerSocketChannel
                .localAddress(new InetSocketAddress(port)) // 设置 Netty Server 的端口
                .option(ChannelOption.SO_BACKLOG, 1024) // 服务端 accept 队列的大小
                .childOption(ChannelOption.SO_KEEPALIVE, true) // TCP Keepalive 机制，实现 TCP 层级的心跳保活功能
                .childOption(ChannelOption.TCP_NODELAY, true) // 允许较小的数据包的发送，降低延迟
                .childHandler(nettyServerHandlerInitializer);


        // 绑定端口，并同步等待成功，即启动服务端
        ChannelFuture future = serverBootstrap.bind().sync();
        if (future.isSuccess()) {
            channel = future.channel();
            log.info("[start][Netty Server 启动在 {} 端口]", port);
        }

    }


    /**
     * 关闭 Netty Server
     */
    @PreDestroy
    public void shutdown() {
        // 关闭 Netty Server
        if (channel != null) {
            channel.close();
        }
        // 优雅关闭两个 EventLoopGroup 对象
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
