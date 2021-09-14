package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.transport.IZlibTransport;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.NotSerializableException;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ISerializationTest {

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary", "createPairList",
            "createJson"})
    void serialDeserial(Object method) {
        TMemoryBuffer memoryBuffer = new TMemoryBuffer(4096);
        TTransport zlib = new IZlibTransport(memoryBuffer);
        IObjectProtocol proto = new IObjectProtocol(zlib);

//        boolean rNativ = false;
//        boolean wNativ = false;

        Object obj1 = List.of(method);
        Object obj2;

        try {
            proto.writeObject(obj1, true, true);
            obj2 = proto.readObject();
            assertEquals(obj1, obj2);
        } catch (TException | NotSerializableException e) {
            e.printStackTrace();
            assert false;
        }

    }



    static Stream<Object> createBoolean() {
//        return Stream.of(new Random(12345678).nextBoolean());
        return Stream.of(false);
    }

    static Stream<Object> createByte() {
        return Stream.of(new Random(12345678).nextInt(16));
    }

    static Stream<Object> createShort() {
        return Stream.of(new Random(12345678).nextInt(128));
    }

    static Stream<Object> createInteger() {
        return Stream.of(new Random(12345678).nextInt());
    }

    static Stream<Object> createLong() {
        return Stream.of(new Random(12345678).nextLong());
    }

    static Stream<Object> createDouble() {
        return Stream.of(new Random(12345678).nextDouble());
    }

    static Stream<Object> createString() {
        return Stream.of(new Random(12345678).ints().toString());
    }

    static Stream<Object> createList() {
        Random random = new Random(12345678);
        return Stream.of(List.of(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    static Stream<Object> createSet() {
        Random random = new Random(12345678);
        return Stream.of(Set.of(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    static Stream<Object> createMap() {
        Random random = new Random(12345678);
        return Stream.of(Map.of(random.nextInt(), random.nextDouble(), random.nextInt(), random.nextDouble(),
                random.nextInt(), random.nextDouble()));
    }

    static Stream<Object> createPair() {
        Random random = new Random(12345678);
        return Stream.of(new AbstractMap.SimpleEntry<>(random.nextInt(), random.nextDouble()));
    }

    static Stream<Object> createBinary() {
        Random random = new Random(12345678);
        return Stream.of(new byte[]{
                (byte) random.nextInt(16), (byte) random.nextInt(16),
                (byte) random.nextInt(16), (byte) random.nextInt(16)
        });
    }

    static Stream<Object> createPairList() {
        List<Map.Entry<Integer, String>> elements = List.of(new AbstractMap.SimpleEntry<>(1, "Mateo"),
                new AbstractMap.SimpleEntry<>(3, "Tomas"), new AbstractMap.SimpleEntry<>(17, "Berto"));
        return Stream.of(elements);
    }

    static Stream<Object> createJson() {
        return Stream.of(new JSONObject());
    }
}