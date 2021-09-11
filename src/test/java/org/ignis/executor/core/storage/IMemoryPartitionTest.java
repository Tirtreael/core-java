package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
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

class IMemoryPartitionTest {

//    @BeforeAll
//    void exampleData(){
//    }

    static Stream<Object> createBoolean() {
        return Stream.of(new Random().nextBoolean());
    }

    static Stream<Object> createByte() {
        return Stream.of(new Random().nextInt(16));
    }

    static Stream<Object> createShort() {
        return Stream.of(new Random().nextInt(128));
    }

    static Stream<Object> createInteger() {
        return Stream.of(new Random().nextInt());
    }

    static Stream<Object> createLong() {
        return Stream.of(new Random().nextLong());
    }

    static Stream<Object> createDouble() {
        return Stream.of(new Random().nextDouble());
    }

    static Stream<Object> createString() {
        return Stream.of(new Random().ints().toString());
    }

    static Stream<Object> createList() {
        Random random = new Random();
        return Stream.of(List.of(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    static Stream<Object> createSet() {
        Random random = new Random();
        return Stream.of(Set.of(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    static Stream<Object> createMap() {
        Random random = new Random();
        return Stream.of(Map.of(random.nextInt(), random.nextDouble(), random.nextInt(), random.nextDouble(),
                random.nextInt(), random.nextDouble()));
    }

    static Stream<Object> createPair() {
        Random random = new Random();
        return Stream.of(new AbstractMap.SimpleEntry<>(random.nextInt(), random.nextDouble()));
    }

    static Stream<Object> createBinary() {
        Random random = new Random();
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

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair"/*, "createBinary"*/,
            "createPairList"/*, "createJson"*/})
    void readWriteNativ(Object method) {
        int size = 1;
        boolean rNativ = false;
        IMemoryPartition partition1 = new IMemoryPartition(10);

        try {

            this.read(method, partition1, rNativ);

            assertEquals(List.of(method), partition1.getElements());

        } catch (TException | NotSerializableException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    void iterator() {
    }


    /*
    UTILITY METHODS
     */
    /*
        Read from protocol to partitions
     */
    void read(Object elements, IMemoryPartition partition, boolean nativ) throws TException, NotSerializableException {
        TTransport memoryBuffer = new TMemoryBuffer(4096);
        TTransport zlib = new IZlibTransport(memoryBuffer);
        IObjectProtocol proto = new IObjectProtocol(zlib);

        proto.writeObject(elements, nativ, true);
        zlib.flush();
        partition.read(memoryBuffer);
    }

    /*Object write(IMemoryPartition partition, boolean nativ) throws TException, NotSerializableException {
        TTransport memoryBuffer = new TMemoryBuffer(4096);
        partition.write(memoryBuffer, 0, nativ);
        TTransport zlib = new IZlibTransport(memoryBuffer);
        IObjectProtocol proto = new IObjectProtocol(zlib);
        return proto.readObject();
    }*/

/*

    @Test
    void testClone() {
    }

    @Test
    void testWrite() {
    }

    @Test
    void testWrite1() {
    }

    @Test
    void readIterator() {
    }

    @Test
    void writeIterator() {
    }

    @Test
    void copyFrom() {
    }

    @Test
    void moveFrom() {
    }

    @Test
    void size() {
    }

    @Test
    void toBytes() {
    }

    @Test
    void clear() {
    }

    @Test
    void fit() {
    }

    @Test
    void type() {
    }

*/

}