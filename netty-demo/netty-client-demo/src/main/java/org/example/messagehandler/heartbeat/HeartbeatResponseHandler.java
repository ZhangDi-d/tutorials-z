package org.example.messagehandler.heartbeat;


import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.example.dispatcher.MessageHandler;
import org.example.message.heartbeat.HeartbeatResponse;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class HeartbeatResponseHandler implements MessageHandler<HeartbeatResponse> {


    @Override
    public void execute(Channel channel, HeartbeatResponse message) {
        log.info("[execute][收到连接({}) 的心跳响应]", channel.id());
    }

    @Override
    public String getType() {
        return HeartbeatResponse.TYPE;
    }

}
