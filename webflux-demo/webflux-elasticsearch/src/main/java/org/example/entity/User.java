package org.example.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author dizhang
 * @date 2021-03-19
 */
@Document(indexName = "idx_user")
public class User implements Serializable {

    @Id
    private String id;

    private Long timestamp;

    private String data;

    public User() {
    }

    public User(String id, Long timestamp, String data) {
        this.id = id;
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", data='" + data + '\'' +
                '}';
    }
}
