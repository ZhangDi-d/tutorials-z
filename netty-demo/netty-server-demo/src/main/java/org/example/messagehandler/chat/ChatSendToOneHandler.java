package org.example.messagehandler.chat;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.example.codec.Invocation;
import org.example.dispatcher.MessageHandler;
import org.example.message.chat.ChatRedirectToUserRequest;
import org.example.message.chat.ChatSendResponse;
import org.example.message.chat.ChatSendToOneRequest;
import org.example.server.NettyChannelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dizhang
 * @date 2021-04-15
 */
@Log4j2
@Component
public class ChatSendToOneHandler implements MessageHandler<ChatSendToOneRequest> {

    @Autowired
    private NettyChannelManager nettyChannelManager;

    @Override
    public void execute(Channel channel, ChatSendToOneRequest message) {
        // 这里，假装直接成功
        ChatSendResponse sendResponse = new ChatSendResponse().setMsgId(message.getMsgId()).setCode(0);
        channel.writeAndFlush(new Invocation(ChatSendResponse.TYPE, sendResponse));

        // 创建转发的消息，并广播发送
        ChatRedirectToUserRequest sendToUserRequest = new ChatRedirectToUserRequest().setMsgId(message.getMsgId())
                .setContent(message.getContent());
        nettyChannelManager.send(message.getToUser(),new Invocation(ChatRedirectToUserRequest.TYPE, sendToUserRequest));
    }

    @Override
    public String getType() {
        return ChatSendToOneRequest.TYPE;
    }
}
