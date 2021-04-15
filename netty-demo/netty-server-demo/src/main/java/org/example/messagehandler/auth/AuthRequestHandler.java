package org.example.messagehandler.auth;

import io.netty.channel.Channel;
import org.example.codec.Invocation;
import org.example.dispatcher.MessageHandler;
import org.example.message.auth.AuthRequest;
import org.example.message.auth.AuthResponse;
import org.example.server.NettyChannelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author dizhang
 * @date 2021-04-15
 */
@Component
public class AuthRequestHandler implements MessageHandler<AuthRequest> {

    @Autowired
    private NettyChannelManager nettyChannelManager;

    @Override
    public void execute(Channel channel, AuthRequest authRequest) {
// 如果未传递 accessToken
        if (StringUtils.isEmpty(authRequest.getAccessToken())) {
            AuthResponse authResponse = new AuthResponse().setCode(1).setMessage("认证 accessToken 未传入");
            channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
            return;
        }

        // ... 此处应有一段

        // 将用户和 Channel 绑定
        // 考虑到代码简化，我们先直接使用 accessToken 作为 User
        nettyChannelManager.addUser(channel, authRequest.getAccessToken());

        // 响应认证成功
        AuthResponse authResponse = new AuthResponse().setCode(0);
        channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
    }

    @Override
    public String getType() {
        return null;
    }
}
