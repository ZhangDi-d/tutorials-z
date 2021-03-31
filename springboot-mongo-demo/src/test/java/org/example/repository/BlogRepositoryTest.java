package org.example.repository;

import org.example.AbstractTest;
import org.example.entity.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author dizhang
 * @date 2021-03-18
 */

class BlogRepositoryTest extends AbstractTest {
    @Autowired
    private BlogRepository blogRepository;

    @Test
    public void testInsert() {

        Blog product = new Blog();
        product.setName("how to make money 333..");
        blogRepository.insert(product);
        System.out.println(product.getId());
    }

}