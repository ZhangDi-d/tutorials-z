package org.example.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author dizhang
 * @date 2021-03-18
 */
@Component  // 成为Bean 加入到filter chain中
@Order(1) //添加 @Order 注解，设置过滤器的顺序
public class CustomWebFilter implements WebFilter {

    private final Logger logger = LoggerFactory.getLogger(CustomWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        return webFilterChain.filter(serverWebExchange)
                .doOnSuccess(unused -> logger.info("doOnSuccess..."));
    }
}
