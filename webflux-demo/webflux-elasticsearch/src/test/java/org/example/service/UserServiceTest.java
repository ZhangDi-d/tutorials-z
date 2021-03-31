package org.example.service;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.example.AbstractTest;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.example.constant.Constants.USER_ES_INDEX;

/**
 * @author dizhang
 * @date 2021-03-26
 * <p>
 * <p>
 * 执行报错 ：  java.lang.NoSuchMethodError: io.netty.util.internal.AppendableCharSequence.setLength(I)V  ——> netty 包重复了，maven依赖解决了就可以了
 * @See org/example/service/UserService2.java
 */
class UserServiceTest extends AbstractTest {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Resource
    UserService userService;
    @Resource
    ReactiveElasticsearchClient reactiveElasticsearchClient;

    @BeforeEach
    public void before() {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(USER_ES_INDEX);

        reactiveElasticsearchClient.indices()
                .existsIndex(request)
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable))
                .flatMap(indexExists -> {
                    logger.info("Index {} exists: {}", USER_ES_INDEX, indexExists);
                    if (!indexExists) {
                        return createIndex();
                    } else {
                        return Mono.empty();
                    }
                })
                .block();
    }


    private Mono<Boolean> createIndex() {
        String jsonStr = """
                {
                    "properties":{
                        "id":{
                            "type":"keyword"
                        },    
                        "data":{
                            "type":"text"
                        },
                        "timestamp":{
                            "type":"long"
                        }
                    }
                }
                """;
        CreateIndexRequest request = new CreateIndexRequest();
        request.index(USER_ES_INDEX);
        request.mapping(jsonStr, XContentType.JSON);

        return reactiveElasticsearchClient.indices()
                .createIndex(request)
                .doOnSuccess(aVoid -> logger.info("Created Index {}", USER_ES_INDEX))
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }


    @Test
    void findUserById() {
        Mono<User> user = userService.findUserById("001");
        System.out.println(user.block());
    }

    @Test
    void findAllUsers() {
        Flux<User> users = userService.findAllUsers("data", "zhangsan");
        users.toStream(10).forEach(System.out::println);
    }

    @Test
    void saveUser() {
        User user0 = new User("001", 1616664046101L, "zhangsan");
        User user1 = new User("002", 1616664046102L, "zhangsan");
        User user2 = new User("003", 1616664046103L, "lisi");
        User user3 = new User("004", 1616664046104L, "wangwu");
        List<User> users = Arrays.asList(user0, user1, user2, user3);
        for (User user : users) {
            Mono<User> mono = userService.saveUser(user);
            System.out.println(mono.block());
        }
    }

    @Test
    void deleteUserById() {
        Mono<String> mono = userService.deleteUserById("003");
        System.out.println(mono.block());
    }
}
