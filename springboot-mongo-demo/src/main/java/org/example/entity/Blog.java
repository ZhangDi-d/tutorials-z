package org.example.entity;

import org.example.sequence.IncIdEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * @author dizhang
 * @date 2021-03-18
 */
@Document
public class Blog extends IncIdEntity<BigInteger> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
