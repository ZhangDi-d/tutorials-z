package com.example.websocketdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

/**
 * @author dizhang
 * @date 2021-03-16
 * <p>
 * <p>
 * <p>
 * WebSocket 是底层协议，SockJS 是WebSocket 的备选方案，也是底层协议，而 STOMP 是基于 WebSocket（SockJS） 的上层协议。
 * <p>
 * STOMP即Simple (or Streaming) Text Orientated Messaging Protocol，简单(流)文本定向消息协议，一个非常简单和容易实现的协议，提供了可互操作的连接格式
 */
@Configuration
@EnableWebSocketMessageBroker  //启用STOMP
public class WebConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * addEndpoint定义了端点，可以理解为客户端连接地址，连接成功即可使用webSocket的API,setAllowedOrigins(“*”)定义了可以跨域，可以限制域名，
     * 比如 .setAllowedOrigins({"http://www.localhost.com"})， withSockJS()方法定义了支持SockJS连接，优先使用原生的WebSocket，如果浏览器不支持，则降级使用SockJs
     * <p>
     * 一些浏览器中缺少对WebSocket的支持，SockJS是一种备选解决方案。SockJS优先使用原生WebSocket，如果在不支持websocket的浏览器中，会自动降级为轮询的方式。 它在浏览器和web服务器之间创建了一个低延迟、全双工、跨域通信通道。
     * SockJS所处理的URL是“http://”或“https://”模式，而不是“ws://”和“wss://”
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //端点可以添加多个
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();


        //=======================more=======================
        /*
         * 1. 将 /serviceName/stomp/websocketJs路径注册为STOMP的端点，用户连接了这个端点后就可以进行websocket通讯，支持socketJs
         * 2. setAllowedOrigins("*")表示可以跨域
         * 3. withSockJS()表示支持sockJS访问
         * 4. addInterceptors 添加自定义拦截器，这个拦截器是上一个demo自己定义的获取httpsession的拦截器
         * 5. addInterceptors 添加拦截处理，这里MyPrincipalHandshakeHandler 封装的认证用户信息
         */
        /*registry.addEndpoint("/stomp/websocketJS")
                //.setAllowedOrigins("*")
                .addInterceptors(new WebSocketHandshakeInterceptor())
                .setHandshakeHandler(new MyPrincipalHandshakeHandler())
                .withSockJS()
        ;*/

    }


    /**
     * 配置消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 内置消息代理
        //以/app开头的destination header的STOMP消息被路由到@Controller类中的@MessageMapping方法。
        registry.setApplicationDestinationPrefixes("/app");
        //使用内置的消息代理进行订阅和广播，并将destination header 以/topic开头的消息路由到代理 ,支持可变参数
        registry.enableSimpleBroker("/topic");



        //=======================more=======================
        /*
         *  enableStompBrokerRelay 配置外部的STOMP服务，需要安装额外的支持 比如rabbitmq或activemq
         * 1. 配置代理域，可以配置多个，此段代码配置代理目的地的前缀为 /topicTest 或者 /userTest
         *    我们就可以在配置的域上向客户端推送消息
         * 3. 可以通过 setRelayHost 配置代理监听的host,默认为localhost
         * 4. 可以通过 setRelayPort 配置代理监听的端口，默认为61613
         * 5. 可以通过 setClientLogin 和 setClientPasscode 配置账号和密码
         * 6. setxxx这种设置方法是可选的，根据业务需要自行配置，也可以使用默认配置
         */
        //registry.enableStompBrokerRelay("/topicTest","/userTest")
        //.setRelayHost("rabbit.someotherserver")
        //.setRelayPort(62623);
        //.setClientLogin("userName")
        //.setClientPasscode("password")
        //;


        // 自定义调度器，用于控制心跳线程
        /*ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setThreadNamePrefix("websocket-heartbeat-thread-");
        taskScheduler.initialize();

        registry.enableSimpleBroker("/topicTest","/userTest")
                .setHeartbeatValue(new long[]{10000,10000})
                .setTaskScheduler(taskScheduler);*/
        /*
         *  "/app" 为配置应用服务器的地址前缀，表示所有以/app 开头的客户端消息或请求
         *  都会路由到带有@MessageMapping 注解的方法中
         */
        //registry.setApplicationDestinationPrefixes("/app");


        /*
         * 自定义路径分割符
         * 注释掉的这段代码添加的分割符为. 分割是类级别的@messageMapping和方法级别的@messageMapping的路径
         * 例如类注解路径为 “topic”,方法注解路径为“hello”，那么客户端JS stompClient.send 方法调用的路径为“/app/topic.hello”
         * 注释掉此段代码后，类注解路径“/topic”,方法注解路径“/hello”,JS调用的路径为“/app/topic/hello”
         */
        //registry.setPathMatcher(new AntPathMatcher("."));
    }


    // 配置发送与接收的消息参数，可以指定消息字节大小，缓存大小，发送超时时间
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        /*
         * 1. setMessageSizeLimit 设置消息缓存的字节数大小 字节
         * 2. setSendBufferSizeLimit 设置websocket会话时，缓存的大小 字节
         * 3. setSendTimeLimit 设置消息发送会话超时时间，毫秒
         */
        registry.setMessageSizeLimit(10240)
                .setSendBufferSizeLimit(10240)
                .setSendTimeLimit(10000);
    }

    // 设置输入消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        /*
         * 配置消息线程池
         * 1. corePoolSize 配置核心线程池，当线程数小于此配置时，不管线程中有无空闲的线程，都会产生新线程处理任务
         * 2. maxPoolSize 配置线程池最大数，当线程池数等于此配置时，不会产生新线程
         * 3. keepAliveSeconds 线程池维护线程所允许的空闲时间，单位秒
         */
        registration.taskExecutor().corePoolSize(10)
                .maxPoolSize(20)
                .keepAliveSeconds(60);
        /*
         * 添加stomp自定义拦截器，可以根据业务做一些处理
         * springframework 4.3.12 之后版本此方法废弃，代替方法 interceptors(ChannelInterceptor... interceptors)
         * 消息拦截器，实现ChannelInterceptor接口
         */
        //registration.setInterceptors(webSocketChannelInterceptor());
    }

    // 设置输出消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(10)
                .maxPoolSize(20)
                .keepAliveSeconds(60);
        //registration.setInterceptors(new WebSocketChannelInterceptor());
    }

    // 自定义控制器方法的参数类型，有兴趣可以google HandlerMethodArgumentResolver这个的用法
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

    }
    // 自定义控制器方法返回值类型，有兴趣可以google HandlerMethodReturnValueHandler这个的用法
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }
    // 添加自定义的消息转换器，spring 提供多种默认的消息转换器，返回false,不会添加消息转换器，返回true，会添加默认的消息转换器，当然也可以把自己写的消息转换器添加到转换链中
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return false;
    }


    /**
     * 拦截器加入 spring ioc容器
     * @return
     */
/*    @Bean
    public WebSocketChannelInterceptor webSocketChannelInterceptor()
    {
        return new WebSocketChannelInterceptor();
    }*/

}
