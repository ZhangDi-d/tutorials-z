package org.example.service;

import org.elasticsearch.index.query.QueryBuilders;
import org.example.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.example.constant.Constants.USER_ES_INDEX;

/**
 * @author dizhang
 * @date 2021-03-25
 */
@Service
public class UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;

    private final ReactiveElasticsearchClient reactiveElasticsearchClient;

    public UserService(ReactiveElasticsearchOperations reactiveElasticsearchOperations,
                       ReactiveElasticsearchClient reactiveElasticsearchClient) {
        this.reactiveElasticsearchOperations = reactiveElasticsearchOperations;
        this.reactiveElasticsearchClient = reactiveElasticsearchClient;
    }


    public Mono<User> findUserById(String id) {

        return reactiveElasticsearchOperations.get(
                id,
                User.class,
                IndexCoordinates.of(USER_ES_INDEX)
        ).doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }

    public Flux<User> findAllUsers(String field, String value) {

        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();

        if (!StringUtils.isEmpty(field) && !StringUtils.isEmpty(value)) {

            query.withQuery(QueryBuilders.matchQuery(field, value));
        }

        return reactiveElasticsearchOperations.search(
                query.build(),
                User.class,
                IndexCoordinates.of(USER_ES_INDEX)
        )
                .map(SearchHit::getContent)
                .filter(Objects::nonNull)
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }

    public Mono<User> saveUser(User User) {
        return reactiveElasticsearchOperations.save(
                User,
                IndexCoordinates.of(USER_ES_INDEX)
        ).doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }

    public Mono<String> deleteUserById(String id) {

        return reactiveElasticsearchOperations.delete(
                id,
                IndexCoordinates.of(USER_ES_INDEX)
        ).doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }

}
