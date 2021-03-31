package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

/**
 * @author dizhang
 * @date 2021-03-25
 */
@Configuration
@EnableReactiveElasticsearchRepositories  // 开启响应式的 Elasticsearch 的 Repository 的自动化配置
public class ElasticsearchReactiveConfig  { //extends AbstractReactiveElasticsearchConfiguration

//    @Override
//    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
//        return ReactiveRestClients.create(ClientConfiguration.builder().connectedTo("127.0.0.1:9200").build());
//    }

//    @Value("${spring.data.elasticsearch.client.reactive.endpoints}")
//    private String elassandraHostAndPort;
//
//    @Bean
//    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo(elassandraHostAndPort)
//                .withWebClientConfigurer(webClient -> {
//                    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
//                            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
//                            .build();
//                    return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
//                })
//                .build();
//
//        return ReactiveRestClients.create(clientConfiguration);
//    }
//
//    @Bean
//    public ElasticsearchConverter elasticsearchConverter() {
//        return new MappingElasticsearchConverter(elasticsearchMappingContext());
//    }
//
//    @Bean
//    public SimpleElasticsearchMappingContext elasticsearchMappingContext() {
//        return new SimpleElasticsearchMappingContext();
//    }
//
//    @Bean
//    public ReactiveElasticsearchOperations reactiveElasticsearchOperations() {
//        return new ReactiveElasticsearchTemplate(reactiveElasticsearchClient(), elasticsearchConverter());
//    }


}
