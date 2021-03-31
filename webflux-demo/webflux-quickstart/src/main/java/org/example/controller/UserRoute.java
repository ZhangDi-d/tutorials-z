package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author dizhang
 * @date 2021-03-17
 */
@Configuration
public class UserRoute {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoute.class);

    @Resource
    private UserService userService;

    @Resource
    private UserRouteHandler userRouteHandler;

    @Bean
    public RouterFunction<ServerResponse> getRoute() {
        return RouterFunctions.route(RequestPredicates.GET("/user2/find"), serverRequest -> {
            Optional<String> user_id = serverRequest.queryParam("user_id");
            User user = userService.get(Integer.valueOf(user_id.get()));
            return ServerResponse.ok().bodyValue(user);
        });
    }

    //处理异常 onErrorReturn
    @Bean
    public RouterFunction<ServerResponse> getRoute2() {
        return RouterFunctions.route(
                RequestPredicates
                        .GET("/user2/find2")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                serverRequest -> userRouteHandler.handleRequestReturn(serverRequest));
    }

    //处理异常 onErrorResume
    @Bean
    public RouterFunction<ServerResponse> getRoute3() {
        return RouterFunctions.route(
                RequestPredicates
                        .GET("/user2/find3")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                serverRequest -> userRouteHandler.handleRequestResume(serverRequest));
    }

    //处理异常 global
    @Bean
    public RouterFunction<ServerResponse> getRouteGlobal() {
        return RouterFunctions.route(
                RequestPredicates
                        .GET("/user2/find4")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                serverRequest -> userRouteHandler.handleRequestGlobal(serverRequest));
    }


    @Bean
    public RouterFunction<ServerResponse> getRouteWebFilter() {
        return RouterFunctions.route(RequestPredicates.GET("/user2/find5"), serverRequest -> {
            Optional<String> user_id = serverRequest.queryParam("user_id");
            User user = userService.get(Integer.valueOf(user_id.get()));
            return ServerResponse.ok().bodyValue(user);

        }).filter(new HandlerFilterFunction<ServerResponse, ServerResponse>() {
            @Override
            public Mono<ServerResponse> filter(ServerRequest serverRequest, HandlerFunction<ServerResponse> handlerFunction) {

                return handlerFunction
                        .handle(serverRequest)
                        .doOnSuccess(unused -> LOGGER.info("getRouteWebFilter doOnSuccess..."));
            }
        });
    }


}
