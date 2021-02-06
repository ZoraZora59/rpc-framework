package com.zora.rpc.demo.provider.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.demo.provider.handler</h4>
 * <p>测试处理器</p>
 *
 * @author zora
 * @since 2021.01.07
 */
@Slf4j
public class DemoChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        log.info("客户端连上了...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //可以在这里面写一套类似SpringMVC的框架
        //让SimpleServerHandler不跟任何业务有关，可以封装一套框架
        if (msg instanceof ByteBuf) {
            log.info("数据读取=[{}]", ((ByteBuf) msg).toString(Charset.defaultCharset()));
        }

        //业务逻辑代码处理框架。。。

        //返回给客户端的数据，告诉我已经读到你的数据了
        String result = "hello client ";
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(result.getBytes());
        ctx.channel().writeAndFlush(buf);
        log.info("第一轮返回结果{}", result);

        String pause = "\r\n";
        ByteBuf buf2 = Unpooled.buffer();
        buf2.writeBytes(pause.getBytes());
        ctx.channel().writeAndFlush(buf2);
        log.info("第二轮返回结果{}", pause);
    }
}
