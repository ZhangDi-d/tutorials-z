package example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author dizhang
 * @date 2021-04-18
 */
//@Configuration
//public class WebConfig extends WebMvcConfigurationSupport {
//    @Override
//    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
//        return new RestfulRequestMappingHandlerMapping();
//    }
//}




//@Configuration   //PathPatternParser
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer.setPatternParser(new PathPatternParser());
//    }
//}