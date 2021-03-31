package org.example.serializer;


import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class ProtoSerializer<T> implements RedisSerializer<T> {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final ThreadLocal<ProtoObject> PROTO_OBJECT = ThreadLocal.withInitial(ProtoObject::new);

    private static class ProtoWrapper {
        private Object data;
    }

    private static class ProtoObject {
        private final Schema<ProtoWrapper> schema;
        private final LinkedBuffer buffer;
        private final ProtoWrapper wrapper;

        public ProtoObject() {
            this.schema = RuntimeSchema.getSchema(ProtoWrapper.class);
            this.buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            this.wrapper = new ProtoWrapper();
        }
    }

    public ProtoSerializer() {
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public byte[] serialize(T t) throws SerializationException {
        if (null == t) {
            return EMPTY_BYTE_ARRAY;
        }
        var proto = PROTO_OBJECT.get();
        proto.wrapper.data = t;
        try {
            return ProtostuffIOUtil.toByteArray(proto.wrapper, proto.schema, proto.buffer);
        } finally {
            proto.buffer.clear();
        }
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public T deserialize(byte[] bytes) throws SerializationException {
        if (null == bytes || bytes.length <= 0) {
            return null;
        }
        var proto = PROTO_OBJECT.get();
        var newMessage = proto.schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, newMessage, proto.schema);
        return (T) newMessage.data;
    }

}
