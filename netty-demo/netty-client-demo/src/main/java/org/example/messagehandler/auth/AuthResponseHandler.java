package org.example.messagehandler.auth;


import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.example.dispatcher.MessageHandler;
import org.example.message.auth.AuthResponse;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AuthResponseHandler implements MessageHandler<AuthResponse> {


    @Override
    public void execute(Channel channel, AuthResponse message) {
        log.info("[execute][认证结果：{}]", message);
    }

    @Override
    public String getType() {
        return AuthResponse.TYPE;
    }

}
