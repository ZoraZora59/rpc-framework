package com.zora.rpc.demo.consumer.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.nio.charset.Charset;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.demo.consumer.handler</h4>
 * <p></p>
 *
 * @author zora
 * @since 2021.01.07
 */
public class SimpleHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            String value = ((ByteBuf) msg).toString(Charset.defaultCharset());
            System.out.println("服务器端返回的数据:" + value);
        }

        AttributeKey<String> key = AttributeKey.valueOf("ServerData");
        ctx.channel().attr(key).set("客户端处理完毕");

        //把客户端的通道关闭
        ctx.channel().close();
    }
}
