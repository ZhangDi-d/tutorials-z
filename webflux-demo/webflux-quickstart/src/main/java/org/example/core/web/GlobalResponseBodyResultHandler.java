package org.example.core.web;

import org.example.core.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

/**
 * @author dizhang
 * @date 2021-03-18
 * <p>
 * 该类将 Response 的 body 写回给前端
 */
public class GlobalResponseBodyResultHandler extends ResponseBodyResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalResponseBodyResultHandler.class);

    private static final MethodParameter METHOD_PARAMETER_MONO_COMMON_RESULT;

    private static final Response COMMON_RESULT_SUCCESS = Response.success(null);

    static {
        try {
            // <1> 获得 METHOD_PARAMETER_MONO_COMMON_RESULT 。其中 -1 表示 `#methodForParams()` 方法的返回值
            METHOD_PARAMETER_MONO_COMMON_RESULT = new MethodParameter(
                    GlobalResponseBodyResultHandler.class.getDeclaredMethod("methodForParams"), -1);
        } catch (NoSuchMethodException e) {
            LOGGER.error("[static][获取 METHOD_PARAMETER_MONO_COMMON_RESULT 时, 找不到方法");
            throw new RuntimeException(e);
        }
    }

    public GlobalResponseBodyResultHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver) {
        super(writers, resolver);
    }

    public GlobalResponseBodyResultHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver, ReactiveAdapterRegistry registry) {
        super(writers, resolver, registry);
    }


    @Override
    public boolean supports(HandlerResult result) {
        LOGGER.info("enter supports ...");
        return super.supports(result);
    }

    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object body;
        Object returnValue = result.getReturnValue();
        if(returnValue instanceof Mono){
            body = ((Mono<Object>) returnValue).map((Function<Object, Object>) GlobalResponseBodyResultHandler::wrapCommonResult).defaultIfEmpty(COMMON_RESULT_SUCCESS);

        }else if(returnValue instanceof Flux){
            body = ((Flux<?>) returnValue).collectList().map((Function<Object, Object>) GlobalResponseBodyResultHandler::wrapCommonResult).defaultIfEmpty(COMMON_RESULT_SUCCESS);
        }else{
            body = wrapCommonResult(returnValue);
        }
        return writeBody(body,METHOD_PARAMETER_MONO_COMMON_RESULT,exchange);

    }

    private static Mono<Response> methodForParams() {
        return null;
    }

    private static Response<?> wrapCommonResult(Object body) {
        return body instanceof Response ? (Response<?>) body : Response.success(body);
    }

}
