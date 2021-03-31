package org.example.config;

import org.example.entity.User;
import org.example.serializer.ProtoSerializer;
import org.example.serializer.SnappySerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @author dizhang
 * @date 2021-03-19
 */
@Configuration
public class RedisConfiguration {


//    @Bean
//    @Primary
//    public ReactiveRedisConnectionFactory lettuceConnectionFactory() {
//
//        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
//                .useSsl().and()
//                .commandTimeout(Duration.ofSeconds(2))
//                .shutdownTimeout(Duration.ZERO)
//                .build();
//
//        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("127.0.0.1", 6379), clientConfig);
//    }

    @Bean
    public ReactiveRedisTemplate<String, Object> commonRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, Object> serializationContext =
                RedisSerializationContext.<String, Object>newSerializationContext(RedisSerializer.string())
                        .value( RedisSerializer.json() )   // 创建通用的 GenericJackson2JsonRedisSerializer //作为序列化 new SnappySerializer(new ProtoSerializer<>())
                        .build();
        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }

    @Bean
    public ReactiveRedisTemplate<String, User> userRedisTemplate(ReactiveRedisConnectionFactory factory) {

        RedisSerializationContext<String, User> serializationContext =
                RedisSerializationContext.<String, User>newSerializationContext(RedisSerializer.string())
                        .value(new Jackson2JsonRedisSerializer<>(User.class)) // new SnappySerializer(new ProtoSerializer<User>()) 创建专属 User 的 Jackson2JsonRedisSerializer 作为序列化
                        .build();
        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }

}
