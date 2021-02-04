package com.zora.rpc.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.client.handler</h4>
 * <p>服务请求处理</p>
 *
 * @author zora
 * @since 2021.02.04
 */
public class ServerHandler  extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();
        channelGroup.forEach(ch ->{
            if(channel != ch){
                ch.writeAndFlush(channel.remoteAddress()+"发送消息："+s+"\n");
            }else{
                ch.writeAndFlush("[ 自己 ]"+s+"\n");
            }
        });
    }
}
