package org.example.messagehandler.chat;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.example.dispatcher.MessageHandler;
import org.example.message.chat.ChatSendResponse;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ChatSendResponseHandler implements MessageHandler<ChatSendResponse> {


    @Override
    public void execute(Channel channel, ChatSendResponse message) {
        log.info("[execute][发送结果：{}]", message);
    }

    @Override
    public String getType() {
        return ChatSendResponse.TYPE;
    }

}
