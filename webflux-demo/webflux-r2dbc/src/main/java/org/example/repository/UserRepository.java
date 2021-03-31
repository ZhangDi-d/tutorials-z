package org.example.repository;

import org.example.entity.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * @author dizhang
 * @date 2021-03-26
 */
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    /**
     * find by username
     *
     * @param username username
     * @return Mono<User>
     */
    @Query("SELECT id, username, password, create_time FROM users WHERE username = :username")
    Mono<User> findByUsername(String username);


    @Modifying
    @Query("update users u set u.username = :username,u.password =:password where u.id= :id")
    Mono<Boolean> updateById(@Param(value = "username") String username, @Param(value = "password") String password, @Param(value = "id") Integer id);
}
