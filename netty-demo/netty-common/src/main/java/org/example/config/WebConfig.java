package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author dizhang
 * @date 2021-04-15
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
//    @Override
//    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
//        return new RestfulRequestMappingHandlerMapping();
//    }



    @Override
    protected RestRequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new RestRequestMappingHandlerMapping();
    }

        @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPatternParser(new PathPatternParser());
    }
}


