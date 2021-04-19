package example.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dizhang
 * @date 2021-04-18
 */
public class RestfulRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final Map<HandlerMethod, RequestMappingInfo> mappingLookup = new LinkedHashMap<>();


    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        //自己的查找逻辑，如果找不到，再执行原有的逻辑，以免出现错误情况
        HandlerMethod handlerMethod = lookupHandlerMethodHere(lookupPath, request);
        if (handlerMethod == null)
            handlerMethod = super.lookupHandlerMethod(lookupPath, request);
        return handlerMethod;
    }

    //自己的查找逻辑，根据从请求头中获取服务名service-name，进行匹配查找
    private HandlerMethod lookupHandlerMethodHere(String lookupPath, HttpServletRequest request) {
        String serviceName = request.getHeader("service_name");
        if (!StringUtils.isEmpty(serviceName)) {
            List<HandlerMethod> methodList = this.getHandlerMethodsForMappingName(serviceName);
            if (!CollectionUtils.isEmpty(methodList) && methodList.size() > 0) {
                HandlerMethod handlerMethod = methodList.get(0);
                RequestMappingInfo requestMappingInfo = mappingLookup.get(handlerMethod);
                handleMatch(requestMappingInfo, lookupPath, request);
                return handlerMethod;
            }
        }
        return null;
    }

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        HandlerMethod handlerMethod = createHandlerMethod(handler, method);
        mappingLookup.put(handlerMethod, mapping);
        super.registerHandlerMethod(handler, method, mapping);
    }
}
