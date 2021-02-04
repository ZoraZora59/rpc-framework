package com.zora.rpc.client.conn;

import com.alibaba.fastjson.JSON;
import com.zora.rpc.client.client.ClintInitializer;
import com.zora.rpc.client.server.ServerInitializer;
import com.zora.rpc.common.model.RpcRequest;
import com.zora.rpc.common.model.RpcResponse;
import com.zora.rpc.common.model.ServiceMetaData;
import com.zora.rpc.common.thread.DefaultThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.common.model</h4>
 * <p>rpc 连接保持器</p>
 *
 * @author zora
 * @since 2021.02.04
 */
@Slf4j
public class RpcConnector implements IConnector {
    private ExecutorService executorService = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1), new DefaultThreadFactory.Builder().build());
    private volatile ServerBootstrap serverBootstrap;
    private volatile Bootstrap bootstrap;
//    private InputStream inputStream;
    private volatile LinkedBlockingDeque<String> messageQueue = new LinkedBlockingDeque<>();

    public RpcConnector(int localPort, ServiceMetaData metaData) {
        executorService.execute(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            serverBootstrap = new ServerBootstrap();
            try {

                serverBootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class).
                        childHandler(new ServerInitializer());
                ChannelFuture channelFuture = serverBootstrap.bind(localPort).sync();//绑定端口
                channelFuture.channel().closeFuture().sync();
            } catch (Exception exception) {
                log.error("服务端创建失败", exception);
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
        executorService.execute(() -> {
            EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

            try {
                bootstrap = new Bootstrap();
                bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).
                        handler(new ClintInitializer());
                Channel channel = bootstrap.connect(metaData.getIpAddress(), metaData.getPort())
//                        .sync()
                        .channel();

                while (true) {
                    channel.writeAndFlush(messageQueue.poll() + "\r\n");
                }
            } catch (Exception e) {
                log.error("客户端创建失败", e);
            } finally {
                eventLoopGroup.shutdownGracefully();
            }
        });
    }

    @Override
    public RpcResponse<?> call(RpcRequest request) {
        String json = JSON.toJSONString(request);
        messageQueue.add(json);
        return null;
    }

    @Override
    public RpcResponse<?> callAsync(RpcRequest request) {
        return null;
    }
}
