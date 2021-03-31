package org.example.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Random;

/**
 * @author dizhang
 * @date 2021-03-18
 */
@Component
public class UserRouteHandler {


    public Mono<ServerResponse> handleRequestReturn(ServerRequest request) {
        return sayHello(request)
                .onErrorReturn("Hello Stranger")
                .flatMap(s -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(s));
    }

    public Mono<ServerResponse> handleRequestResume(ServerRequest request) {
        return sayHello(request)
                .flatMap(s -> ServerResponse.ok()    //正常返回
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(s))
                .onErrorResume(e -> Mono.just("ERROR " + e.getMessage())  // 异常恢复
                        .flatMap(s -> ServerResponse.ok()
                                .contentType(MediaType.TEXT_PLAIN)
                                .bodyValue(s)));
    }

    public Mono<ServerResponse> handleRequestGlobal(ServerRequest serverRequest) {
        return sayHello(serverRequest)
                .flatMap(s -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(s));
    }

    private Mono<String> sayHello(ServerRequest request) {
        Random random = new Random();
        try {
            if (random.nextBoolean()) throw new RuntimeException("exception...");
            return Mono.just("hello");
        } catch (Exception e) {
            return Mono.error(e);
        }
    }


}
