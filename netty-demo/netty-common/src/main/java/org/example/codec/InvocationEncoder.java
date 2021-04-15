package org.example.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.log4j.Log4j2;

/**
 * @author dizhang
 * @date 2021-04-15
 */
@Log4j2
public class InvocationEncoder extends MessageToByteEncoder<Invocation> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Invocation invocation, ByteBuf byteBuf) throws Exception {
        byte[] bytes = JSON.toJSONBytes(invocation);
        byteBuf.writeBytes(bytes);
        byteBuf.writeInt(bytes.length);
        log.info("[encode][连接({}) 编码了一条消息({})]", channelHandlerContext.channel().id(), invocation.toString());
    }
}
