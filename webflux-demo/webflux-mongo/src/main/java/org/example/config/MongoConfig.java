package org.example.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

/**
 * @author dizhang
 * @date 2021-03-18
 */
@Configuration
public class MongoConfig implements InitializingBean {

//    @Resource
//    MongoDatabaseFactory mongoDatabaseFactory;
//    @Resource
//    MongoMappingContext mongoMappingContext;

//    @Bean
//    public MappingMongoConverter mappingMongoConverter() {
//
//        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
//        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
//        //设置 typeMapper 属性，从而移除 _class field
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//
//        return converter;
//    }



        //在reactive情况下，使用上面的凡是会报错，使用下面的方式似乎更简单而且不会报错 。
        @Autowired
        @Lazy
        private MappingMongoConverter mappingMongoConverter;

        @Override
        public void afterPropertiesSet() {
            mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        }

}
