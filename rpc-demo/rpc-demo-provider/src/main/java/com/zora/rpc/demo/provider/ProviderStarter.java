package com.zora.rpc.demo.provider;

import com.zora.rpc.common.thread.DefaultThreadFactory;
import com.zora.rpc.demo.api.Constants;
import com.zora.rpc.demo.provider.handler.DemoChannelHandler;
import com.zora.rpc.serialize.util.RpcSerializeUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.demo.provider</h4>
 * <p>生产者启动器</p>
 *
 * @author zora
 * @since 2021.01.05
 */
@Slf4j
public class ProviderStarter {
    public static void main(String[] args) throws InterruptedException {
        nettyServer();
    }

    static void nettyServer() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        EventLoopGroup clientExecutors = new NioEventLoopGroup();
        try {
            bootstrap.group(eventExecutors, clientExecutors).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel arg0) throws Exception {
                    arg0.pipeline().addLast(new DemoChannelHandler());

                }
            }).option(ChannelOption.SO_BACKLOG, 1024);//指定此套接口排队的最大连接个数)
            ChannelFuture f = bootstrap.bind(Constants.NETTY_PORT).sync();
            f.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
            clientExecutors.shutdownGracefully();
        }
    }

    static void socketServer() throws InterruptedException {
        ExecutorService serverSocketExecutor = new ThreadPoolExecutor(2, 2,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                new DefaultThreadFactory.Builder().namePrefix("SERVER_SOCKET").single(true).build());
        Semaphore semaphore = new Semaphore(0);

        serverSocketExecutor.execute(new ServerRunner(semaphore));
//        serverSocketExecutor.execute(new ServerRunner(semaphore));

        semaphore.acquire();
    }

    static class ServerRunner implements Runnable {
        Semaphore semaphore;
        ExecutorService socketExecutor = new ThreadPoolExecutor(2, 8,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4),
                new DefaultThreadFactory.Builder().namePrefix("socket-pool").build());

        ServerRunner(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(Constants.PORT)) {
                AtomicInteger counter = new AtomicInteger(0);
                while (true) {
                    Socket socket = serverSocket.accept();
                    socketExecutor.execute(() -> {
                        try {
                            log.info("socket建立连接，连接信息「ip = {} , port = {} , hashcode = {}」", socket.getInetAddress().toString(), socket.getPort(), socket.hashCode());
                            log.info("socket收到数据[{}]", RpcSerializeUtil.deserializeRequest(socket.getInputStream()).toString());
                            Thread.sleep(5000);
                            socket.close();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    log.info("轮询-{}-完成", counter.incrementAndGet());
                }
            } catch (IOException e) {
                log.error("服务器线程遇到问题", e);
            } finally {
                semaphore.release();
            }
        }
    }
}
