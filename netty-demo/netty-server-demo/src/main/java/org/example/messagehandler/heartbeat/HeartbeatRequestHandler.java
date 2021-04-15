package org.example.messagehandler.heartbeat;


import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.example.codec.Invocation;
import org.example.dispatcher.MessageHandler;
import org.example.message.heartbeat.HeartbeatRequest;
import org.example.message.heartbeat.HeartbeatResponse;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class HeartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {

    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        log.info("[execute][收到连接({}) 的心跳请求]", channel.id());
        // 响应心跳
        HeartbeatResponse response = new HeartbeatResponse();
        channel.writeAndFlush(new Invocation(HeartbeatResponse.TYPE, response));
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }

}
