package org.example.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.xerial.snappy.Snappy;

import java.util.Objects;

public class SnappySerializer<T> implements RedisSerializer<T> {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private final RedisSerializer<T> innerSerializer;

    public SnappySerializer(RedisSerializer<T> innerSerializer) {
        this.innerSerializer = innerSerializer;
    }

    @Override
    public byte[] serialize(T graph) throws SerializationException {
        if (graph == null) {
            return EMPTY_BYTE_ARRAY;
        }
        try {
            return Snappy.compress(Objects.requireNonNull(innerSerializer.serialize(graph)));
        } catch (Exception e) {
            throw new SerializationException("Snappy Serialization Error", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return innerSerializer.deserialize(Snappy.uncompress(bytes));
        } catch (Exception e) {
            throw new SerializationException("Snappy deserialize error", e);
        }
    }
}
