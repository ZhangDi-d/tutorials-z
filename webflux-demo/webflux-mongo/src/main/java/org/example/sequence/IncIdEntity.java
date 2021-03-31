package org.example.sequence;

import org.springframework.data.annotation.Id;

/**
 * @author dizhang
 * @date 2021-03-18
 */
public abstract class IncIdEntity<T extends Number> {

    @Id
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
