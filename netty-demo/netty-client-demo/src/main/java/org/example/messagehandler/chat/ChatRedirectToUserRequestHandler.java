package org.example.messagehandler.chat;


import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.example.dispatcher.MessageHandler;
import org.example.message.chat.ChatRedirectToUserRequest;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ChatRedirectToUserRequestHandler implements MessageHandler<ChatRedirectToUserRequest> {


    @Override
    public void execute(Channel channel, ChatRedirectToUserRequest message) {
        log.info("[execute][收到消息：{}]", message);
    }

    @Override
    public String getType() {
        return ChatRedirectToUserRequest.TYPE;
    }

}
