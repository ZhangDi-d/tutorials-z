package org.example.repository;

import org.example.entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author dizhang
 * @date 2021-03-18
 */
public interface BlogRepository extends MongoRepository<Blog, Integer> {
}
