package com.zora.rpc.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.client.handler</h4>
 * <p>消息处理</p>
 *
 * @author zora
 * @since 2021.02.04
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("Receive message : {}",msg);
    }
}
