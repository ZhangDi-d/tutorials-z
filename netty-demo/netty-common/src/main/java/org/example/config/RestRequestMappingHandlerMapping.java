package org.example.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dizhang
 * @date 2021-04-19
 */


public class RestRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    private static final Map<String, HandlerMethod> NAME_HANDLER_MAP = new HashMap<>();
    private static final Map<HandlerMethod, RequestMappingInfo> MAPPING_HANDLER_MAP = new HashMap<>();

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        HandlerMethod handlerMethod = createHandlerMethod(handler, method);
        RequestMapping requestMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        assert requestMapping != null;

        String[] headers = requestMapping.headers();
        for (String header : headers) {
            if (header.contains("service_name")){
                NAME_HANDLER_MAP.put(header.split("=")[1], handlerMethod);
            }
        }
        MAPPING_HANDLER_MAP.put(handlerMethod, mapping);
        System.out.println("headers: " + Arrays.toString(requestMapping.headers()) + ", handlerMethod: " + handlerMethod.toString());
        super.registerHandlerMethod(handler, method, mapping);
    }

    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        String service_name = request.getHeader("service_name");
        HandlerMethod handlerMethod = NAME_HANDLER_MAP.get(service_name);
        if (StringUtils.isNotBlank(service_name) && handlerMethod != null) {
            handleMatch(MAPPING_HANDLER_MAP.get(handlerMethod), lookupPath, request);
            return handlerMethod;
        }
        return super.lookupHandlerMethod(lookupPath, request);
    }

}