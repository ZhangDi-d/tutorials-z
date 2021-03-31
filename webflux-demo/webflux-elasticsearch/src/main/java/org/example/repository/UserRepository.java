package org.example.repository;

import org.example.entity.User;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;

/**
 * @author dizhang
 * @date 2021-03-26
 */
public interface UserRepository extends ReactiveElasticsearchRepository<User, String> {
}
